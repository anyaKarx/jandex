package com.jandex.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "parent_id",  nullable = true)
    private Category parentId;

    @Column(name = "price", nullable = true)
    private Long price;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<History> histories = new ArrayList<>();

    public Category(UUID parentId) {
        this.id = parentId;
    }

    public void setAveragePrice(Long price) {
        if ( price != null)
            this.price = price;
        else  this.price = Long.valueOf(0);
    }


    public void setParentId(UUID parentId) {
        if(parentId != null)
        this.parentId = new Category(parentId);
        else this.parentId = null;
    }
}
