package com.jandex.repository;

import com.jandex.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    @Modifying
    @Transactional
    @Query("update Offer o set o.date = ?1, o.price = ?2, o.parent = ?3, o.name = ?4 where o.id = ?5")
    void update(LocalDateTime date, Long price, UUID parent, String name, UUID id);

    List<Offer> findAllByParent(UUID parent);
}
