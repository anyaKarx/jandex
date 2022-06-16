package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "History")
public class History {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update owner id", nullable = false)
    private UUID externalIdParent;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    @Column(name = "Price", nullable = false)
    private Long price;
}


