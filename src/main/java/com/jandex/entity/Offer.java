package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @Column(name = "id", nullable = false)
    private UUID Id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category parent;

    @Column(name = "price", nullable = false)
    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories;

}
