package com.jandex.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime date;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "price", nullable = true)
    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Offer> offers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_category_id", foreignKey = @ForeignKey(name = "fk_category_parent_category"))
    @JsonBackReference
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    public void addParentAndChildren(Category parent) {
        this.setParentCategory(parent);
        if (parent.getChildren() == null) {
            List<Category> children = new ArrayList<>();
            children.add(this);
            parent.setChildren(children);
        } else {
            parent.getChildren().add(this);
        }
    }

    public Long getAvgPrice() {
        if (children.size() == 0) {
            return this.price = (long) this.getOffers().stream()
                    .collect(summarizingLong(Offer::getPrice)).getAverage();
        } else {
            var summingPrice = (Long) children.stream().mapToLong(Category::getAvgPriceOffers).sum();
            var countOffers = (Integer) children.stream().mapToInt(Category::getCountOffers).sum();
            return this.price = summingPrice / (countOffers == 0 ? 1 : countOffers);
        }
    }

    public Long getAvgPriceOffers() {
        return (long) (Long) this.getOffers().stream().mapToLong(Offer::getPrice).sum();

    }

    public int getCountOffers() {

        return this.getOffers().size();
    }
}
