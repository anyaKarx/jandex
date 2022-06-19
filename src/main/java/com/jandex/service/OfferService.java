package com.jandex.service;

import com.jandex.entity.Category;
import com.jandex.entity.Offer;
import com.jandex.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deleteByUUID(UUID id) {
        offerRepository.deleteById(id);
    }


    @Transactional
    public void deleteByParent(Category parent) {
        offerRepository.deleteAllByParent(parent);
    }

    public int countOfferByParentId(UUID parentId) {
        return offerRepository.countOfferByParentId(parentId);
    }
}
