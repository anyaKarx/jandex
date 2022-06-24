package com.jandex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jandex.dto.ShopUnitImportDTO;
import com.jandex.exception.IncorrectDataException;
import com.jandex.service.GoodsService;
import com.jandex.web.rest.ShopUnitController;
import com.utils.BuilderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jandex.config.Constans.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
public class ShopUnitControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShopUnitController shopUnitController;

    @MockBean
    private GoodsService goodsService;

    @Test
    void shouldReturnBadRequestWhenOfferDidntValidParentId() throws Exception {
        var category = BuilderHelper.createShopUnitImportDTOCategory();
        var offer = BuilderHelper.createShopUnitImportDTOOffer().setParentId(UUID.randomUUID());
        List<ShopUnitImportDTO> importDTOList = new ArrayList<>(List.of(category, offer));
        var request = BuilderHelper.createShopUnitImportRequestDTO(importDTOList);
        final String requestJsonBody = objectMapper.writeValueAsString(request);

        given(goodsService.importsData(any())).willThrow(new IncorrectDataException(""));

        mvc.perform(
                post(API_PREFIX + WEB_PATH + API_VERSION + "/imports")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(requestJsonBody)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
