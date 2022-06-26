package com.jandex.web.rest;

import com.jandex.dto.*;
import com.jandex.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static com.jandex.config.Constans.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + WEB_PATH + API_VERSION)
public class ShopUnitController {

    private final GoodsService goodsService;

    @Operation(summary = "Импортирует новые товары и/или категории.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Вставка или обновление прошли успешно.",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @PostMapping("/imports")
    public ResponseDTO saveUnitImport(@RequestBody @Valid ShopUnitImportRequestDTO request) {
        return goodsService.importsData(request);
    }

    @Operation(summary = "Удалить элемент по идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Удаление прошло успешно.",
                            content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Категория/товар не найден.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @DeleteMapping("/delete/{id}")
    public ResponseDTO delete(@PathVariable @Valid UUID id) {
        return goodsService.delete(id);
    }

    @Operation(summary = "Получить информацию об элементе по идентификатору. ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Информация об элементе.",
                            content = @Content(schema = @Schema(implementation = ShopUnitDTO.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Категория/товар не найден.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @GetMapping("/nodes/{id}")
    public ShopUnitDTO getNodes(@PathVariable @Valid UUID id) {
        return goodsService.getNodes(id);
    }

    @Operation(summary = "Получение списка товаров, цена которых была обновлена за последние 24" +
            " часа включительно [now() - 24h, now()] от времени переданном в запросе.  ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Информация об измененных товарах",
                            content = @Content(schema = @Schema(implementation = ShopUnitDTO.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Категория/товар не найден.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @GetMapping("/sales")
    public ShopUnitStatisticResponseDTO sales(@RequestParam(name = "date") String date) {
        return goodsService.getSales(date);
    }

    @Operation(summary = "Получение статистики (истории обновлений)" +
            " по товару/категории за заданный полуинтервал [from, to). ",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Информация об измененных товарах",
                            content = @Content(schema = @Schema(implementation = ShopUnitDTO.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Невалидная схема документа или входные данные не верны.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Категория/товар не найден.",
                            content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    @GetMapping("/node/{id}/statistic")
    public ShopUnitStatisticResponseDTO statistic(@PathVariable @Valid UUID id,
                                                  @RequestParam(name = "dateStart") String dateStart,
                                                  @RequestParam(name = "dateEnd") String dateEnd) {
        return goodsService.getStatistic(id, dateStart, dateEnd);
    }
}
