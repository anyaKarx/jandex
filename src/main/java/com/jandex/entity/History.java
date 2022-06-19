package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_parent", nullable = false)
    private Offer parent;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "price", nullable = false)
    private Long price;
}


