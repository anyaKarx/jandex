package com.jandex.config;

import io.goodforgod.jackson.module.datetime.configuration.JavaTimeModuleConfiguration;

public class DateConfig {
    JavaTimeModuleConfiguration configuration = JavaTimeModuleConfiguration.ofISO()
 .setZonedDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setLocalDateTimeFormat("uuuu-MM-dd'T'HH:mm:ss.SSS");

}
