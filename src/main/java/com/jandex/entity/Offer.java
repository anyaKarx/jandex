package com.jandex.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.ZZZ")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @Column(name = "price", nullable = false)
    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> histories;

}
