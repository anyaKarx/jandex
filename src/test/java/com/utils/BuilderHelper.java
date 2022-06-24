package com.utils;

import com.jandex.dto.ShopUnitImportDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.dto.ShopUnitTypeDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class BuilderHelper {
    public static ShopUnitImportRequestDTO createShopUnitImportRequestDTO(List<ShopUnitImportDTO> items) {
        return new ShopUnitImportRequestDTO()
                .setItems(items)
                .setUpdateDate(LocalDateTime.now().minusDays(10L));
    }

    public static ShopUnitImportDTO createShopUnitImportDTOOffer() {
        return new ShopUnitImportDTO()
                .setId(UUID.randomUUID())
                .setName(randomAlphabetic(10))
                .setPrice(new Random().nextLong())
                .setType(ShopUnitTypeDTO.OFFER)
                .setParentId(null);
    }

    public static ShopUnitImportDTO createShopUnitImportDTOCategory() {
        return new ShopUnitImportDTO()
                .setId(UUID.randomUUID())
                .setName(randomAlphabetic(10))
                .setType(ShopUnitTypeDTO.CATEGORY)
                .setParentId(null)
                .setPrice(0L);
    }
}
