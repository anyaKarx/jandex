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
@Table(name = "Offer")
public class Offer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;

    @Column(name = "idExternal", nullable = false)
    private UUID externalId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    @Column(name = "Parent idExternal", nullable = false)
    private UUID parentId;

    @Column(name = "Price", nullable = false)
    private Long price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<History> histories = new ArrayList<>();
}
