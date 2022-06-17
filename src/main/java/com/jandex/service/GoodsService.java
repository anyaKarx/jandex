package com.jandex.service;

import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.dto.ShopUnitTypeDTO;
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
    private final GoodsMapper goodsMapper;

    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        var itemsToEntity = items.stream()
                .map(shopUnitImportDTO -> goodsMapper.importToShopUnit(shopUnitImportDTO, request.getUpdateDate()))
                .collect(groupingBy(ShopUnitDTO::getType));
        var category = itemsToEntity.get(ShopUnitTypeDTO.CATEGORY);
        var offers = itemsToEntity.get(ShopUnitTypeDTO.OFFER);
        if(category != null){
        saveCategory(category, request.getUpdateDate());}
        if (offers != null){
        saveOffer(offers, request.getUpdateDate());}
        return new ResponseDTO()
                .setResultCode(HttpStatus.OK.value())
                .setResultMessage(HttpStatus.OK.name());
    }

    public void saveCategory(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToCategory)
                .peek(category -> category.getHistories().add(
                        new History()
                                .setDate(dateTime)
                                .setPrice(category.getPrice()).setCategory(category)))
                .forEach(categoryService::save);
    }

    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToOffer)
                .map(this::setCategory)
                .peek(offer -> {
                    offer.getHistories().add(
                                new History()
                                .setDate(dateTime)
                                .setPrice(offer.getPrice())
                                .setOffer(offer));

                })
                .forEach(offerService::save);
    }

    public Offer setCategory(Offer offer) {
        var category =offer.getParentId();
         category.setPrice(offer.getPrice());
         category.getHistories().add(
                new History()
                        .setDate(offer.getDate())
                        .setPrice(category.getPrice()).setCategory(category));
         category.setDate(offer.getDate());
         while(category.getParentId() != null){
            category = category.getParentId();
             category.getHistories().add(
                     new History()
                             .setDate(offer.getDate())
                             .setPrice(category.getPrice()).setCategory(category));
             category.setDate(offer.getDate());
             category.setPrice(offer.getPrice());
         }
        return offer;
    }
}
