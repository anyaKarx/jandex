package com.jandex.mapper;


import com.jandex.entity.History;
import com.jandex.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface HistoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offer", ignore = true)
    History offerToHistory(Offer offer);
}
