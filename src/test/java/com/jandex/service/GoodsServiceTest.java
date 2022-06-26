package com.jandex.service;

import com.jandex.AbstractIT;
import com.jandex.dto.ShopUnitImportDTO;
import com.utils.BuilderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoodsServiceTest extends AbstractIT {

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
    public void deleteShopInitTest() {
        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var category1 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category2 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category3 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category1.getId());

        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer1 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer2 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer3 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer4 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, category1, category2, category3, offer,
                offer1, offer2, offer3, offer4));

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
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, category1, category2, category3, offer,
                offer1, offer2, offer3, offer4));

        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);

        goodsService.importsData(request);

        var result = goodsService.getNodes(category.getId());

        assertEquals(category.getId(), result.getId());
    }


    @Test
    public void statsOfferTest() {

        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var category1 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category2 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category3 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category1.getId());

        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer1 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer2 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer3 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer4 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        var offer5 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, category1, category2, category3, offer,
                offer1, offer2, offer3, offer4, offer5));

        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);

        goodsService.importsData(request);
        offer = offer.setPrice(10000L);
        offer1 = offer1.setPrice(10060L);
        offer2 = offer2.setPrice(107L);
        offer3 = offer3.setPrice(108700L);
        offer4 = offer4.setPrice(56L);
        importDTOList = new ArrayList<>(List.of(offer,
                offer1, offer2, offer3, offer4, offer5));
        var request2 = BuilderHelper.createShopUnitImportRequestDTO(importDTOList).setUpdateDate(LocalDateTime.now().minusDays(8L));
        goodsService.importsData(request2);
        var result = goodsService.getStatistic(offer.getId(), LocalDateTime.now().minusDays(11L).toString().substring(0, 24), LocalDateTime.now().minusDays(8L).toString().substring(0, 24));

        assertEquals(offer.getId(), result.getItems().get(0).getId());
    }

    @Test
    @Transactional
    public void statsCategoryTest() {

        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var category1 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category2 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category.getId());
        var category3 = BuilderHelper.createShopUnitImportDTOCategory().setParentId(category1.getId());

        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category2.getId());
        var offer4 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());
        var offer5 = BuilderHelper.createShopUnitImportDTOOffer().setParentId(category3.getId());

        offer = offer.setPrice(1500L);
        offer4 = offer4.setPrice(1500L);
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, category1, category2, category3, offer,
                 offer4, offer5));

        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);
        goodsService.importsData(request);
        offer = offer.setPrice(10000L);
        offer4 = offer4.setPrice(5660L);
        importDTOList = new ArrayList<>(List.of(offer,
                offer4, offer5));
        var request2 = BuilderHelper.createShopUnitImportRequestDTO(importDTOList).setUpdateDate(LocalDateTime.now().minusDays(8L));

        var result = goodsService.getStatistic(category.getId(), LocalDateTime.now().minusDays(11L).toString().substring(0, 24), LocalDateTime.now().minusDays(8L).toString().substring(0, 24));
        goodsService.importsData(request2);
        result = goodsService.getStatistic(category.getId(), LocalDateTime.now().minusDays(11L).toString().substring(0, 24), LocalDateTime.now().minusDays(8L).toString().substring(0, 24));
        assertEquals(category.getId(), result.getItems().get(0).getId());
      //  assertEquals(7, result.getItems().size());
    }
}
