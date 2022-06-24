package com.jandex.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.ZZZ")
    private LocalDateTime date;

    @OnDelete(action = OnDeleteAction.CASCADE)
    private UUID parent;

    @Column(name = "price", nullable = false)
    private Long price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id.equals(offer.id) && name.equals(offer.name) && date.equals(offer.date) && price.equals(offer.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, price);
    }
}
