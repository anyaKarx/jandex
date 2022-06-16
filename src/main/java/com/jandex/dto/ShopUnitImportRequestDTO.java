package com.jandex.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUnitImportRequestDTO {
    @Parameter(description = "Импортируемые элементы")
    private List<ShopUnitImportDTO> items;

    @Parameter(description = "Время обновления добавляемых товаров/категорий.")
    private LocalDateTime updateDate;

}
