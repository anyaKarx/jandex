package com.jandex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDTO {
    @JsonProperty("code")
    private Integer code = null;

    @JsonProperty("message")
    private String message = null;
}
