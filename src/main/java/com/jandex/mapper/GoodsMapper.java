package com.jandex.mapper;

import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportDTO;
import com.jandex.entity.Category;
import com.jandex.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper
public interface GoodsMapper {
    @Mapping(target = "children", ignore = true)
    ShopUnitDTO importToShopUnit(ShopUnitImportDTO shopUnitImportDTO, LocalDateTime date);


    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "internalId", ignore = true)
    @Mapping(target = "offers", ignore = true)
    @Mapping(target = "histories", ignore = true)
    Category shopUnitToCategory(ShopUnitDTO shopUnitDTO);

    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "internalId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "histories", ignore = true)
    Offer shopUnitToOffer(ShopUnitDTO shopUnitDTO);

}
