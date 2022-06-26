package com.jandex.service;

import com.jandex.entity.Category;
import com.jandex.entity.Offer;
import com.jandex.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public Offer save(Offer offer) {
        return offerRepository.saveAndFlush(offer);
    }

    public void update(LocalDateTime date, Long price, UUID parent, String name, UUID id) {
        offerRepository.update(date, price, parent, name, id);
    }

    public Optional<Offer> getOfferByUUID(UUID id) {
        return offerRepository.findById(id);
    }

    public List<Offer> findOffersByParent(Category parent) {
        return offerRepository.findAllByParent(parent.getId());
    }

    public void deleteByUUID(UUID id) {
        offerRepository.deleteById(id);
    }
}
