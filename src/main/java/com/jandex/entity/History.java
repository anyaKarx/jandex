package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "history")
public class History {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "category")
    private UUID parentId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "price", nullable = false)
    private Long price;
}


