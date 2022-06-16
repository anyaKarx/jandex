package com.jandex.service;

import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.dto.ShopUnitTypeDTO;
import com.jandex.entity.History;
import com.jandex.entity.Offer;
import com.jandex.mapper.GoodsMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
public class GoodsService {
    private final CategoryService categoryService;
    private final OfferService offerService;
    private final GoodsMapper goodsMapper;

    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        var itemsToEntity = items.stream()
                .map(shopUnitImportDTO -> goodsMapper.importToShopUnit(shopUnitImportDTO, request.getUpdateDate()))
                .collect(groupingBy(ShopUnitDTO::getType));
        saveCategory(itemsToEntity.get(ShopUnitTypeDTO.CATEGORY), request.getUpdateDate());
        saveOffer(itemsToEntity.get(ShopUnitTypeDTO.OFFER), request.getUpdateDate());
        return new ResponseDTO()
                .setResultCode(HttpStatus.OK.value())
                .setResultMessage(HttpStatus.OK.name());
    }

    public void saveCategory(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToCategory)
                .map(category -> {
                    category.getHistories().add(
                            new History()
                                    .setDate(dateTime)
                                    .setPrice(category.getPrice()));
                    return category;
                })
                .map(categoryService::save);
    }

    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToOffer)
                .map(this::setCategory)
                .map(offer -> {
                    offer.getHistories().add(
                            new History()
                                    .setDate(dateTime)
                                    .setPrice(offer.getPrice())
                                    .setOffer(offer));
                    return offer;
                })
                .map(offerService::save);
    }

    public Offer setCategory(Offer offer) {
        offer.setCategory(categoryService.getByExternalId(offer.getParentId()));
        categoryService.getByExternalId(offer.getParentId()).setPrice(offer.getPrice());
        return offer;
    }
}
