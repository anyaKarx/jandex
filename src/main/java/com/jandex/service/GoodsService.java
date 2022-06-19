package com.jandex.service;

import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.dto.ShopUnitTypeDTO;
import com.jandex.entity.Category;
import com.jandex.entity.History;
import com.jandex.exception.IncorrectDataException;
import com.jandex.mapper.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        } else recursDelete(categoryService.getCategoryByUUID(id).get());
        return ResponseDTO.builder().resultCode(HttpStatus.OK.value()).resultMessage(HttpStatus.OK.name()).build();
    }

    public ShopUnitDTO getNodes(UUID id) {
        ShopUnitDTO shopUnitDTO = new ShopUnitDTO();
        if (categoryService.getCategoryByUUID(id).isEmpty()) {
            if (offerService.getOfferByUUID(id).isEmpty()) {
                throw new IncorrectDataException("Категория/товар не найден.");
            } else {
                shopUnitDTO = goodsMapper.offerToShopUnitDto(offerService.getOfferByUUID(id).get());
            }
            throw new IncorrectDataException("Категория/товар не найден.");
        } else {
            shopUnitDTO = categoryToShopUnitDTO(categoryService.getCategoryByUUID(id).get());
        }
        return shopUnitDTO;
    }

    public ShopUnitDTO categoryToShopUnitDTO(Category category) {
        ShopUnitDTO shopUnitDTO = goodsMapper.categoryToShopUnitDto(category);
        category.getOffers().stream()
                .map(goodsMapper::offerToShopUnitDto)
                .map(shopUnit -> shopUnitDTO.addChildrenItem(shopUnit));
        for (int i = 0; i < category.getChildren().size(); i++) {
            var tempCategory = category.getChildren().get(i);
            var shopUnitCategory = goodsMapper.categoryToShopUnitDto(tempCategory);
            tempCategory.getOffers().stream()
                    .map(goodsMapper::offerToShopUnitDto)
                    .map(shopUnit -> shopUnitCategory.addChildrenItem(shopUnit));
            shopUnitDTO.addChildrenItem(shopUnitCategory);
        }
        return shopUnitDTO;
    }

    @Transactional
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

        return ResponseDTO.builder().resultCode(HttpStatus.OK.value()).resultMessage(HttpStatus.OK.name()).build();
    }

    @Transactional
    public void saveCategory(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToCategory)
                .forEach(category ->
                {
                    if (category.getParentId() != null) {
                        var parent = categoryService
                                .getCategoryByUUID(category.getParentId())
                                .orElseThrow(() -> new IncorrectDataException("e"));
                        category.addParentAndChildren(parent);
                    }
                    categoryService.save(category);
                });
    }

    @Transactional
    public void saveOffer(List<ShopUnitDTO> itemsToEntity, LocalDateTime dateTime) {
        itemsToEntity
                .stream()
                .map(goodsMapper::shopUnitToOffer)
                .forEach(offer -> {
                    var parent = categoryService
                            .getCategoryByUUID(offer.getParent().getId())
                            .orElseThrow(() -> new IncorrectDataException("e"));
                    parent.getOffers().add(offer);
                    parent = categoryService.save(parent);
                    offer.setParent(parent);
                    offerService.save(offer);
                    historyService.save(new History()
                            .setParent(offer)
                            .setDate(dateTime)
                            .setPrice(offer.getPrice()));
                });
    }


    @Transactional
    public void recursDelete(Category category) {
        category = categoryService.save(category);
        categoryService.delete(category);
    }

//    public ShopUnitDTO getNodes(Category category)
//    {
//        var item = new ShopUnitDTO();
//        item.setDate(category.setDate()).setId()
//    }
}
