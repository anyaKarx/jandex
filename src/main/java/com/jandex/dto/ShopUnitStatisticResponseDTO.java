package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShopUnitStatisticResponseDTO {

    @JsonProperty("items")
    @Schema(description = "История в произвольном порядке.")
    @Valid
    private List<ShopUnitStatisticUnitDTO> items = null;

}
