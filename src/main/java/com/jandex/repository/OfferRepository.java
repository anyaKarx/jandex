package com.jandex.repository;

import com.jandex.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findOfferByExternalId(UUID externalId);

}
