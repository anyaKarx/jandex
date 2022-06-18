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

    @Column(name = "parent_id",  nullable = true)
    private UUID parentId;

    @Column(name = "price", nullable = true)
    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Offer> offers = new ArrayList<>();

    public Category(UUID parentId) {
        this.id = parentId;
    }

}
