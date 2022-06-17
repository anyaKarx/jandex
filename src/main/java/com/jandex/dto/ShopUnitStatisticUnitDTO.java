package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ShopUnitStatisticUnitDTO {

    @JsonProperty("id")
    @Schema(description = "История в произвольном порядке.")
    @Valid
    private UUID id;

    @JsonProperty("name")
    @Schema(required = true, description = "Имя элемента")
    @NotNull
    private String name ;

    @JsonProperty("parentId")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", description = "UUID родительской категории")
    @Valid
    private UUID parentId;

    @JsonProperty("type")
    @Schema(required = true, description = "тип: категория или товар")
    @NotNull
    private ShopUnitTypeDTO type;

    @JsonProperty("price")
    @Schema(description = "Целое число, для категории - это средняя цена всех дочерних товаров" +
            "(включая товары подкатегорий). " +
            "Если цена является не целым числом, округляется в меньшую сторону до целого числа." +
            " Если категория не содержит товаров цена равна null.")
    private Long price;

    @JsonProperty("date")
    @Schema(required = true, description = "Время последнего обновления элемента.")
    @NotNull
    @Valid
    private LocalDateTime date;

}
