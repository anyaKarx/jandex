package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class ShopUnitStatisticResponseDTO {

    @JsonProperty("items")
    @Schema(description = "История в произвольном порядке.")
    @Valid
    private List<ShopUnitStatisticUnitDTO> items = null;


    public ShopUnitStatisticResponseDTO addItemsItem(ShopUnitStatisticUnitDTO itemsItem) {
        if (this.items == null) {
            this.items = new ArrayList<ShopUnitStatisticUnitDTO>();
        }
        this.items.add(itemsItem);
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShopUnitStatisticResponseDTO shopUnitStatisticResponse = (ShopUnitStatisticResponseDTO) o;
        return Objects.equals(this.items, shopUnitStatisticResponse.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShopUnitStatisticResponse {\n");

        sb.append("    items: ").append(toIndentedString(items)).append("\n");
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
