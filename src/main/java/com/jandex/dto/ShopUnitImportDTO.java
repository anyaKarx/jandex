package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUnitImportDTO {
    @JsonProperty("id")
    @Parameter(description = "Уникальный идентификатор")
    @NonNull
    private UUID id;

    @JsonProperty("name")
    @Parameter(description = "Имя элемента.")
    @NonNull
    private String name;

    @JsonProperty("parentId")
    @Parameter(description = "UUID родительской категории")
    @Nullable
    private UUID parentId;

    @JsonProperty("type")
    @Parameter(description = "Тип: категория/товар")
    @NotNull
    private ShopUnitTypeDTO type;

    @JsonProperty("price")
    @Parameter(description = "Целое число, для категорий поле должно содержать null.")
    @NotNull
    private Long price;

}
