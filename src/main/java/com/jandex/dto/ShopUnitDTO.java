package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ShopUnitDTO {
    @JsonProperty("id")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", required = true, description = "Уникальный идентификатор")
    @NotNull
    private UUID id;

    @JsonProperty("name")
    @Schema(required = true, description = "Имя категории")
    @NotNull
    private String name;

    @JsonProperty("date")
    @Schema(example = "2022-05-28T21:12:01Z", required = true, description = "Время последнего обновления элемента.")
    @NotNull
    @Valid
    private LocalDateTime date;

    @JsonProperty("parentId")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", description = "UUID родительской категории")
    @Valid
    private UUID parentId;

    @JsonProperty("type")
    @Schema(required = true, description = " тип: категория или товар")
    @NotNull
    @Valid
    private final ShopUnitTypeDTO type;

    @JsonProperty("price")
    @Schema(description = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий)." + " Если цена является не целым числом, округляется в меньшую сторону до целого числа." + " Если категория не содержит товаров цена равна null.")
    private Long price;

    @JsonProperty("children")
    @Schema(description = "Список всех дочерних товаров\\категорий. Для товаров поле равно null.")
    @Valid
    private List<ShopUnitDTO> children = null;


    public ShopUnitDTO addChildrenItem(ShopUnitDTO childrenItem) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(childrenItem);
        return this;
    }
}
