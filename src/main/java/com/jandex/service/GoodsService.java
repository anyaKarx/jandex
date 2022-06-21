package com.jandex.service;

import com.jandex.dto.*;
import com.jandex.entity.Category;
import com.jandex.entity.History;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final HistoryService historyService;
    private final GoodsMapper goodsMapper;

    @Transactional
    public ResponseDTO delete(UUID id) {
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                return ResponseDTO.builder()
                        .resultCode(HttpStatus.NOT_FOUND.value())
                        .resultMessage("Категория/товар не найден.")
                        .build();
            } else {
                offerService.deleteByUUID(id);
            }
            return ResponseDTO.builder()
                    .resultCode(HttpStatus.NOT_FOUND.value())
                    .resultMessage("Категория/товар не найден.")
                    .build();
        } else {
            var category = categoryService.getCategoryByUUID(id).get();
            categoryService.delete(category);
        }
        return ResponseDTO.builder().resultCode(HttpStatus.OK.value()).resultMessage(HttpStatus.OK.name()).build();
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
        category.getOffers().stream()
                .map(goodsMapper::offerToShopUnitDto)
                .forEach(categoryExport::addChildrenItem);
        var priceCategory = category.getAvgPrice();
        categoryExport.setPrice(priceCategory);
        for (int i = 0; i < category.getChildren().size(); i++) {
            var childCategoryExport = categoryToShopUnitDTO(category.getChildren().get(i));
            categoryExport.addChildrenItem(childCategoryExport);
        }
        return categoryExport;
    }

    @Transactional
    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        var itemsToEntity =
                items.stream()
                        .map(shopUnitImportDTO ->
                                goodsMapper
                                        .importToShopUnit(shopUnitImportDTO, request.getUpdateDate()))
                        .collect(groupingBy(ShopUnitDTO::getType));

        var category = itemsToEntity.get(ShopUnitTypeDTO.CATEGORY);
        if (category != null) {
            saveCategory(category, request.getUpdateDate());
        }

        var offers = itemsToEntity.get(ShopUnitTypeDTO.OFFER);
        if (offers != null) {
            saveOffer(offers, request.getUpdateDate());
        }

        return ResponseDTO.builder().resultCode(HttpStatus.OK.value()).resultMessage(HttpStatus.OK.name()).build();
    }

    @Transactional
    public void saveCategory(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToCategory)
                .forEach(category ->
                {
                    if (categoryService.getCategoryByUUID(category.getId()).isPresent()) { // Проверка: записаны ли эти данные уже
                        var old = categoryService.getCategoryByUUID(category.getId()).get();
                        old.setName(category.getName());
                        old.setDate(dateTime);
                        categoryService.save(old);
                    } else {
                        if (category.getParentId() != null) {
                            var parent = categoryService
                                    .getCategoryByUUID(category.getParentId())
                                    .orElseThrow(() -> new IncorrectDataException("e"));
                            category.addParentAndChildren(parent);
                            category.setDate(dateTime);
                        }
                        categoryService.save(category);
                    }
                });
    }

    @Transactional
    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToOffer)
                .forEach(offer -> {
                    if (offerService.getOfferByUUID(offer.getId()).isPresent()) { // Проверка: записаны ли эти данные уже
                        if (dateTime != offerService.getOfferByUUID(offer.getId()).get().getDate()) {
                            var oldOffer = offerService.getOfferByUUID(offer.getId()).get();
                            var history = new History()
                                    .setParent(offer)
                                    .setDate(dateTime)
                                    .setPrice(offer.getPrice());
                            history = historyService.save(history);
                            offer.setHistories(oldOffer.getHistories());
                            offer.addHistory(history);
                            offerService.save(offer);
                        } else {
                            throw new IncorrectDataException("Невалидная схема документа или входные данные не верны.");
                        }
                    } else {
                        var parent = categoryService
                                .getCategoryByUUID(offer.getParent().getId())
                                .orElseThrow(() -> new IncorrectDataException("e"));
                        parent.getOffers().add(offer);
                        setDate(dateTime, parent.getId());
                        parent = categoryService.save(parent);
                        offer.setParent(parent);
                        var history = new History()
                                .setParent(offer)
                                .setDate(dateTime)
                                .setPrice(offer.getPrice());
                        offer = offerService.save(offer);
                        history = historyService.save(history);
                        offer.addHistory(history);
                    }
                });
    }

    public void setDate(LocalDateTime date, UUID parentId) {
        var category = categoryService
                .getCategoryByUUID(parentId)
                .orElseThrow(() -> new IncorrectDataException("e"));
        category.setDate(date);
        if (category.getParentCategory() != null)
            setDate(date, category.getParentCategory().getId());
        categoryService.save(category);
    }

    public ShopUnitStatisticResponseDTO getSales(String dateStr) {

        LocalDateTime one = stringToDate(dateStr);
        LocalDateTime two = one.minusHours(24);

        var changes = historyService
                .getHistoriesByData(two, one).orElseThrow(() -> new IncorrectDataException("e"));
        var offers = changes.stream()
                .map(History::getParent)
                .map(goodsMapper::offerToShopUnitDto)
                .map(goodsMapper::shopUnitDTOToStatDTO)
                .collect(Collectors.toList());

        return new ShopUnitStatisticResponseDTO(offers);
    }

    public ShopUnitStatisticResponseDTO getStatistic(UUID id, String dateStart, String dateEnd) {
        LocalDateTime start = stringToDate(dateStart);
        LocalDateTime end = stringToDate(dateEnd);
        List<ShopUnitStatisticUnitDTO> result = new ArrayList<>();
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                throw new NotFoundDataException("Категория/товар не найден.");
            } else {
                var changes = historyService.getHistoriesByDataAndOffer(start, end, id).get();
                List<ShopUnitDTO> statUnits = new ArrayList<>();
                for (History chang : changes) {
                    var offer = chang.getParent();
                    var statUnit = goodsMapper.offerToShopUnitDto(offer);
                    statUnit.setPrice(chang.getPrice());
                    statUnit.setDate(chang.getDate());
                    statUnits.add(statUnit);
                }
                result = statUnits.stream()
                        .map(goodsMapper::shopUnitDTOToStatDTO).collect(Collectors.toList());
            }
        } else {
            //TODO: в сервисе историй искать историю изменения категории пупупууу;
        }
        return new ShopUnitStatisticResponseDTO(result);
    }

    public LocalDateTime stringToDate(String date) {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");
        date = date.substring(0, date.length() - 1);
        return LocalDateTime.parse(date, formatter);
    }
}
