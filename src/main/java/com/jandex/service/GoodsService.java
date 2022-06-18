package com.jandex.service;

import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.dto.ShopUnitTypeDTO;
import com.jandex.entity.Category;
import com.jandex.entity.History;
import com.jandex.entity.Offer;
import com.jandex.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final HistoryService historyService;
    private final GoodsMapper goodsMapper;

    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        var itemsToEntity =
                items.stream()
                        .map(shopUnitImportDTO -> goodsMapper.importToShopUnit(shopUnitImportDTO, request.getUpdateDate()))
                        .collect(groupingBy(ShopUnitDTO::getType));

        var category = itemsToEntity.get(ShopUnitTypeDTO.CATEGORY);
        if (category != null) {
            saveCategory(category, request.getUpdateDate());
        }

        var offers = itemsToEntity.get(ShopUnitTypeDTO.OFFER);
        if (offers != null) {
            saveOffer(offers, request.getUpdateDate());
        }

        return new ResponseDTO()
                .setResultCode(HttpStatus.OK.value())
                .setResultMessage(HttpStatus.OK.name());
    }

    public void saveCategory(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToCategory)
                .peek(category -> {
                    historyService.save(new History()
                            .setIdParent(category.getId())
                            .setDate(dateTime)
                            .setPrice((category.getPrice() == null ? Long.valueOf(0) : category.getPrice()))
                            .setCategoryId(category.getParentId()));
                })
                .forEach(categoryService::save);
    }

    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToOffer)
                .peek(offer -> {
                    historyService.save(new History()
                            .setIdParent(offer.getId())
                            .setDate(dateTime)
                            .setPrice(offer.getPrice())
                            .setCategoryId(offer.getParent().getId()));
                })
                .forEach(offer -> {
                    offer.setParent(categoryService.get(offer.getParent().getId()));
                    offerService.save(offer);
                    changeCategory(offer, offer.getParent());
                });
    }

    public void changeCategory(Offer offer, Category parent) {
        parent = categoryService.get(parent.getId());
        var history = historyService.findLatestHistoryById(parent.getId());
        var latestPrice = history.getPrice();
        int countOff = offerService.countOfferByParentId(parent.getId());
        Long price = (latestPrice + offer.getPrice())/((countOff == 0) ? 1 : countOff);
        historyService.save(new History()
                        .setDate(offer.getDate())
                        .setPrice(price)
                        .setIdParent(parent.getId()))
                        .setCategoryId(parent.getParentId());
        parent.setDate(offer.getDate());
        categoryService.save(parent);
        while (parent.getParentId() != null) {
            parent = categoryService.get(parent.getParentId());
            changeCategory(offer, parent);
        }
    }
}
