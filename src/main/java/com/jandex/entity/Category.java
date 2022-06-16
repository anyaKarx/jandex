package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
