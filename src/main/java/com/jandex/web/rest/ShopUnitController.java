package com.jandex.web.rest;

import com.jandex.dto.ErrorDTO;
import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import com.jandex.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jandex.config.Constans.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + WEB_PATH + API_VERSION)
public class ShopUnitController {

    private final GoodsService goodsService;

    /**
     * Сохранение товаров и категорий
     *
     * @param request Данные для сохранения
     * @return Результат сохранения
     */
    @Operation(summary = "Поиск договора залога по его id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Вставка или обновление прошли успешно.",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @PostMapping("/imports")
    public ResponseDTO saveUnitImport(@RequestBody ShopUnitImportRequestDTO request) {
        return goodsService.importsData(request);
    }

}
