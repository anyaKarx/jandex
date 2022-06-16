package com.jandex.service;

import com.jandex.dto.ResponseDTO;
import com.jandex.dto.ShopUnitImportRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@AllArgsConstructor
public class GoodsService {
    private final CategoryService categoryService;
    private final OfferService offerService;

    public ResponseDTO importsData(@Valid ShopUnitImportRequestDTO request) {
        var items = request.getItems();
        items.stream()
                .filter(shopUnitImportDTO -> !shopUnitImportDTO.getPrice().equals(null));
//                .map(shopUnitImportDTO -> categoryService.save(shopUnitImportDTO))

        return new ResponseDTO()
                .setResultCode(HttpStatus.OK.value())
                .setResultMessage(HttpStatus.OK.name());
    }

//    public ShopUnitImportDTO saveShopUnit(ShopUnitImportDTO shopUnit) {
//
//    }
}
