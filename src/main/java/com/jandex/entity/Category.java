package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "parent_id", nullable = true)
    private UUID parentId;

    @Column(name = "price", nullable = false)
    private Long price;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<History> histories = new ArrayList<>();

    public void setPrice(Long price) {
        if (this.price != null) {
            Long latestPrice = this.price;
            latestPrice += price;
            latestPrice /= offers.size()+1;
            this.price = latestPrice;
        } if ( price != null)
            this.price = price;
        else  this.price = Long.valueOf(0);
    }
}
