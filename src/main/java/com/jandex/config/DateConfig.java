package com.jandex.config;

import io.goodforgod.jackson.module.datetime.configuration.JavaTimeModuleConfiguration;

public class DateConfig {
    JavaTimeModuleConfiguration configuration = JavaTimeModuleConfiguration.ofISO()
           // .setInstantFormat("uuuu-MM-dd")                                 // Set Instant formatter
           // .setOffsetTimeFormat("HH:mm:ssXXX")                             // Set OffsetTime formatter
           // .setOffsetDateTimeFormat("uuuu-MM-dd'T'HH:mm:ssXXX")            // Set OffsetDateTime formatter
                .setZonedDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");   // Set ZonedDateTime formatter
//            .setLocalDateTimeFormat("uuuu-MM-dd'T'HH:mm:ss")                // Set LocalDateTime formatter
//            .setLocalDateFormat("uuuu-MM-dd")                               // Set LocalDate formatter
//            .setLocalTimeFormat("HH:mm:ssXXX")                              // Set LocalTime formatter
//            .setYearFormat("uuuu")                                          // Set Year formatter
//            .setYearMonthFormat("uuuu-MM")                                  // Set YearMonth formatter
//            .setMonthDayFormat("MM-dd")                                     // Set MonthDay formatter
//            .setForceIsoChronology(true)                                    // Forces IsoChronology for all formatters
//            .setForceResolverStrict(true);                                  // Forces ResolverStyle#STRICT for all formatters
//
}
