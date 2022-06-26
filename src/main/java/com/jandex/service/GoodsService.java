package com.jandex.service;

import com.jandex.dto.*;
import com.jandex.entity.Category;
import com.jandex.entity.CategoryHistory;
import com.jandex.entity.History;
import com.jandex.entity.Offer;
import com.jandex.exception.IncorrectDataException;
import com.jandex.exception.NotFoundDataException;
import com.jandex.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final HistoryService historyService;
    private final GoodsMapper goodsMapper;
    private final CategoryHistoryService categoryHistoryService;

    @Transactional
    public ResponseDTO delete(UUID id) {
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                throw new NotFoundDataException("Категория/товар не найден.");
            } else {
                offerService.deleteByUUID(id);
            }
        } else {
            var category = categoryService.getCategoryByUUID(id).get();
            categoryService.delete(category);
        }
        return ResponseDTO.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMessage(HttpStatus.OK.name())
                .build();
    }

    public ShopUnitDTO getNodes(UUID id) {
        ShopUnitDTO shopUnitDTO;
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                throw new NotFoundDataException("Категория/товар не найден.");
            } else {
                shopUnitDTO = goodsMapper.offerToShopUnitDto(offerService.getOfferByUUID(id).get());
            }
        } else {
            shopUnitDTO = categoryToShopUnitDTO(categoryService.getCategoryByUUID(id).get());
        }
        return shopUnitDTO;
    }

    public ShopUnitDTO categoryToShopUnitDTO(Category category) {
        category = categoryService.save(category);
        ShopUnitDTO categoryExport = goodsMapper.categoryToShopUnitDto(category);
        getOffers(category).stream()
                .map(goodsMapper::offerToShopUnitDto)
                .forEach(categoryExport::addChildrenItem);
        var children = getChildren(category);
        for (Category child : children) {
            var childCategoryExport = categoryToShopUnitDTO(child);
            categoryExport.addChildrenItem(childCategoryExport);
        }
        return categoryExport;
    }

    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        var itemsToEntity =
                items.stream()
                        .map(shopUnitImportDTO -> goodsMapper.importToShopUnit(shopUnitImportDTO, request.getUpdateDate()))
                        .collect(groupingBy(ShopUnitDTO::getType));

        var categories = itemsToEntity.get(ShopUnitTypeDTO.CATEGORY);
        if (categories != null) {
            saveCategory(categories);
        }

        var offers = itemsToEntity.get(ShopUnitTypeDTO.OFFER);
        if (offers != null) {
            saveOffer(offers, request.getUpdateDate());
        }
        return ResponseDTO.builder().resultCode(HttpStatus.OK.value()).resultMessage(HttpStatus.OK.name()).build();
    }


    public void saveCategory(List<ShopUnitDTO> itemsToEntity) {
        itemsToEntity.stream().map(goodsMapper::shopUnitToCategory)
                .forEach(categoryService::save);
    }

    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity.stream().map(goodsMapper::shopUnitToOffer).forEach(offer -> {
            if(offer.getParent() != null){
                categoryService.getCategoryByUUID(offer.getParent())
                        .orElseThrow(() ->
                                new IncorrectDataException("Невалидная схема документа или входные данные не верны."));
            }


            if (offerService.getOfferByUUID(offer.getId()).isPresent()) { // Проверка: записаны ли эти данные уже
                var oldDateTime = offerService.getOfferByUUID(offer.getId()).get().getDate();
                if (!dateTime.equals( oldDateTime)) {
                    offerService.update(offer.getDate(), offer.getPrice(),
                            offer.getParent(), offer.getName(), offer.getId());
                } else {
                    throw new IncorrectDataException("Невалидная схема документа или входные данные не верны.");
                }
            } else {
                offerService.save(offer);
            }

            if(offer.getParent() != null){
                Category parent = categoryService.getCategoryByUUID(offer.getParent())
                        .orElseThrow(() ->
                                new IncorrectDataException("Невалидная схема документа или входные данные не верны."));

            setDateAndPrice(dateTime, parent.getId());}
        });
    }

    public ShopUnitStatisticResponseDTO getSales(String dateStr) {

        LocalDateTime end = stringToDate(dateStr);
        LocalDateTime start = end.minusHours(24);

        var changes = historyService.getHistoriesByData(start, end);
        if (changes.isEmpty()) {
            throw new IncorrectDataException("Невалидная схема документа или входные данные не верны.");
        }
        var offers = changes.stream().map(History::getIdParent)
                .map(offerService::getOfferByUUID)
                .map(Optional::get)
                .map(goodsMapper::offerToShopUnitDto)
                .map(goodsMapper::shopUnitDTOToStatDTO)
                .collect(Collectors.toList());

        return new ShopUnitStatisticResponseDTO(offers);
    }

    public ShopUnitStatisticResponseDTO getStatistic(UUID id, String dateStart, String dateEnd) {
        LocalDateTime start = stringToDate(dateStart);
        LocalDateTime end = stringToDate(dateEnd);
        List<ShopUnitStatisticUnitDTO> result;
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                throw new NotFoundDataException("Категория/товар не найден.");
            } else {
                var changes =
                        historyService.getHistoriesByDataAndOffer(start, end, id);
                List<ShopUnitDTO> statUnits = new ArrayList<>();
                for (History change : changes) {
                    var offer = offerService.getOfferByUUID(change.getIdParent()).get();
                    var statUnit = goodsMapper.offerToShopUnitDto(offer);
                    statUnit.setPrice(change.getPrice());
                    statUnit.setDate(change.getDate());
                    statUnits.add(statUnit);
                }
                result = statUnits.stream()
                        .map(goodsMapper::shopUnitDTOToStatDTO)
                        .collect(Collectors.toList());
            }
        } else {
            var changes =
                    categoryHistoryService.getHistoriesByDataAndCategory(start, end, id);
            List<ShopUnitDTO> statUnits = new ArrayList<>();
            for (CategoryHistory change : changes) {
                var category = categoryService.getCategoryByUUID(change.getIdParent()).get();
                var statUnit = goodsMapper.categoryToShopUnitDto(category);
                statUnit.setPrice(change.getPrice());
                statUnit.setDate(change.getDate());
                statUnits.add(statUnit);
            }
            result = statUnits.stream()
                    .map(goodsMapper::shopUnitDTOToStatDTO)
                    .collect(Collectors.toList());
        }
        return new ShopUnitStatisticResponseDTO(result);
    }

    public LocalDateTime stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");
        date = date.substring(0, date.length() - 1);
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (RuntimeException e) {
            throw new IncorrectDataException("Невалидная схема документа или входные данные не верны.");
        }
    }

    public void setDateAndPrice(LocalDateTime date, UUID parentId) {
        //TODO: заменить триггеры и таблицы историй  на hibernate evners
        var category = categoryService.getCategoryByUUID(parentId)
                .orElseThrow(() ->
                        new IncorrectDataException("Невалидная схема документа или входные данные не верны."));
        category.setDate(date);
        category.setPrice(getAvgPrice(category));
        categoryService.save(category);
        while (category.getParentId() != null)
        {
            category =  categoryService.getCategoryByUUID(category.getParentId())
                    .orElseThrow(() ->
                            new IncorrectDataException("Невалидная схема документа или входные данные не верны."));
            setDateAndPrice(date, category.getId());
        }
    }

    public List<Category> getChildren(Category parent) {
        return categoryService.getChildren(parent);
    }

    public List<Offer> getOffers(Category parent) {
        return offerService.findOffersByParent(parent);
    }

    public Long getAvgPrice(Category category) {
        var children = getChildren(category);
        var offers = getOffers(category);
        if (children.isEmpty()) {
            if (offers.isEmpty()) {
                return (long) 0;
            } else {
                return (long) offers.stream()
                        .collect(summarizingLong(Offer::getPrice))
                        .getAverage();
            }
        } else {
            long priceOffers;
            int countChildOffers;
            if (offers.isEmpty()) {
                priceOffers = 0;
                countChildOffers = 0;
            } else {
                priceOffers = (long) offers.stream()
                        .collect(summarizingLong(Offer::getPrice))
                        .getAverage();
                countChildOffers = offers.size();
            }
            var summingPrice = children.stream()
                    .mapToLong(this::getAvgPriceOffers)
                    .sum() + priceOffers;
            var countOffers = children.stream()
                    .mapToInt(this::getCountOffers)
                    .sum() + countChildOffers;
            return summingPrice / (countOffers == 0 ? 1 : countOffers);
        }
    }

    public Long getAvgPriceOffers(Category category) {
        var offers = offerService.findOffersByParent(category);
        return offers.stream()
                .mapToLong(Offer::getPrice)
                .sum();
    }

    //коммент
    public int getCountOffers(Category category) {
        var offers = offerService.findOffersByParent(category);
        return offers.size();
    }
}
