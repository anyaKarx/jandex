package com.jandex.service;

import com.jandex.JandexApplication;
import com.jandex.dto.ShopUnitImportDTO;
import com.utils.BuilderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = JandexApplication.class)
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void importsDataTest() {
        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category.getId());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, offer));
        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);

        var result = goodsService.importsData(request);

        assertEquals(200, result.getResultCode());
    }

    @Test
    @Transactional
    public void deleteShopInitTest()
    {
        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var category1 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category2 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category3 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category1.getId());

        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer1 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer2 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer3 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer4 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category,category1,category2,category3, offer,
                offer1,offer2,offer3,offer4));

        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);

        goodsService.importsData(request);
        var result = goodsService.delete(category1.getId());

        assertEquals(200, result.getResultCode());
    }

    @Test
    public void nodesToShowTest() {

        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var category1 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category2 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category3 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category1.getId());

        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer1 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer2 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer3 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer4 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category,category1,category2,category3, offer,
                offer1,offer2,offer3,offer4));

        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);

        goodsService.importsData(request);

        var result = goodsService.getNodes(category.getId());

        assertEquals(category.getId(), result.getId());
    }
}
