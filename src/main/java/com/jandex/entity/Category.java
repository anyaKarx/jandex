package com.jandex.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.summarizingLong;

@Getter
@Setter
@Entity
@Table(name = "category")
@Accessors(chain = true)
@RequiredArgsConstructor
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Transient
    private UUID parentId;

    @Column(name = "price", nullable = true)
    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Offer> offers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    public void addParentAndChildren(Category parent) {
        this.setParentCategory(parent);
        if (parent.getChildren() == null) {
            List<Category> childrens = new ArrayList<>();
            childrens.add(this);
            parent.setChildren(childrens);
        } else {
            parent.getChildren().add(this);
        }
    }

    public Long getAvgPrice() {
        return (long) this.getOffers().stream()
                .collect(summarizingLong(Offer::getPrice)).getAverage();
    }
}
