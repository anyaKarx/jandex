package com.jandex.mapper;

import com.jandex.dto.ShopUnitDTO;
import com.jandex.dto.ShopUnitImportDTO;
import com.jandex.dto.ShopUnitStatisticUnitDTO;
import com.jandex.dto.ShopUnitTypeDTO;
import com.jandex.entity.Category;
import com.jandex.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", imports = {ShopUnitTypeDTO.class, DateTimeFormatter.class, ZonedDateTime.class})
public interface GoodsMapper {

    @Mapping(target = "children", ignore = true)
    ShopUnitDTO importToShopUnit(ShopUnitImportDTO shopUnitImportDTO, LocalDateTime date);

    @Mapping(target = "id", source = "id")
   // @Mapping(target = "offers", ignore = true)
    @Mapping(target = "date", source = "date")
    //@Mapping(target = "parentCategory", ignore = true)
    Category shopUnitToCategory(ShopUnitDTO shopUnitDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "parentId",  source = "parentId")
    @Mapping(target = "type", expression = "java(ShopUnitTypeDTO.CATEGORY)")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "children", ignore = true)
    ShopUnitDTO categoryToShopUnitDto(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "parent", source = "parentId")
    Offer shopUnitToOffer(ShopUnitDTO shopUnitDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "parentId", expression = "java(offer.getParent())")
    @Mapping(target = "type", expression = "java(ShopUnitTypeDTO.OFFER)")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "children", ignore = true)
    ShopUnitDTO offerToShopUnitDto(Offer offer);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "date", source = "date")
    ShopUnitStatisticUnitDTO shopUnitDTOToStatDTO(ShopUnitDTO shopUnitDTO);
}
