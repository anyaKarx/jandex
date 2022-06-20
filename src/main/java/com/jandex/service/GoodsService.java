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
                    if (categoryService.getCategoryByUUID(category.getId()).isPresent()) {
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
                    if (offerService.getOfferByUUID(offer.getId()).isPresent()) {
                        if (dateTime != offer.getDate()) {
                            offerService.save(offer);
                            historyService.save(new History()
                                    .setParent(offer)
                                    .setDate(dateTime)
                                    .setPrice(offer.getPrice()));
                        } else {
                            throw new IncorrectDataException("Невалидная схема документа или входные данные не верны.");
                        }
                    }
                    var parent = categoryService
                            .getCategoryByUUID(offer.getParent().getId())
                            .orElseThrow(() -> new IncorrectDataException("e"));
                    parent.getOffers().add(offer);
                    setDate(dateTime, parent.getId());
                    parent = categoryService.save(parent);
                    offer.setParent(parent);
                    offerService.save(offer);
                    historyService.save(new History()
                            .setParent(offer)
                            .setDate(dateTime)
                            .setPrice(offer.getPrice()));
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
       DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");
        dateStr = dateStr.substring(0, dateStr.length() -1);
        LocalDateTime one = LocalDateTime.parse(dateStr, formatter);
        LocalDateTime two = one.minusHours(24);

        var changes = historyService
                .getHistoriesByData(two,one).orElseThrow(()-> new IncorrectDataException("e"));
        var offers = changes.stream()
                .map(History::getParent)
                .map(goodsMapper::offerToShopUnitDto)
                .map(goodsMapper::shopUnitDTOToStatDTO)
                .collect(Collectors.toList());

        return new ShopUnitStatisticResponseDTO(offers);
    }
}
