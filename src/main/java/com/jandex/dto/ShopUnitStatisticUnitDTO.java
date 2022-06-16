package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShopUnitStatisticUnitDTO shopUnitStatisticUnit = (ShopUnitStatisticUnitDTO) o;
        return Objects.equals(this.id, shopUnitStatisticUnit.id) &&
                Objects.equals(this.name, shopUnitStatisticUnit.name) &&
                Objects.equals(this.parentId, shopUnitStatisticUnit.parentId) &&
                Objects.equals(this.type, shopUnitStatisticUnit.type) &&
                Objects.equals(this.price, shopUnitStatisticUnit.price) &&
                Objects.equals(this.date, shopUnitStatisticUnit.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId, type, price, date);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShopUnitStatisticUnit {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    date: ").append(toIndentedString(date)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
