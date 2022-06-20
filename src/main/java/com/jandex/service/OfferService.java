package com.jandex.service;

import com.jandex.entity.Offer;
import com.jandex.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public Offer save(Offer offer) {

        return offerRepository.save(offer);
    }

    public Optional<Offer> getOfferByUUID(UUID id) {
        return offerRepository.findById(id);
    }

    public void deleteByUUID(UUID id) {
        offerRepository.deleteById(id);
    }

   }
