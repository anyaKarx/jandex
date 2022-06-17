package com.jandex.mapper;

import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportDTO;
import com.jandex.entity.Category;
import com.jandex.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GoodsMapper {
    @Mapping(target = "children", ignore = true)
    ShopUnitDTO importToShopUnit(ShopUnitImportDTO shopUnitImportDTO, LocalDateTime date);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "offers", ignore = true)
    @Mapping(target = "histories", ignore = true)
    Category shopUnitToCategory(ShopUnitDTO shopUnitDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "histories", ignore = true)
    Offer shopUnitToOffer(ShopUnitDTO shopUnitDTO);

    default Category map(UUID value){
        return new Category(value);
    }

}
