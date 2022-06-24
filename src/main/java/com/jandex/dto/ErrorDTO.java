package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;
}
