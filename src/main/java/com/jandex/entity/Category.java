package com.jandex.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(name = "parent")
    private UUID parentId;

    @Column(name = "price", nullable = true)
    private Long price;

//    @ManyToOne(fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(name = "parent_category_id", foreignKey = @ForeignKey(name = "fk_category_parent_category"))
//    @JsonBackReference
//    private Category parentCategory;

//    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
//    @JsonManagedReference
//    private List<Category> children = new ArrayList<>();
//
//    public void addParentAndChildren(Category parent) {
//        this.setParentCategory(parent);
//        if (parent.getChildren() == null) {
//            List<Category> children = new ArrayList<>();
//            children.add(this);
//            parent.setChildren(children);
//        } else {
//            parent.getChildren().add(this);
//        }
//    }

//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<CategoryHistory> histories = new ArrayList<>();
//
//    public Category addHistory(CategoryHistory history) {
//        if (this.getHistories() == null) {
//            List<CategoryHistory> historyList = new ArrayList<>();
//            historyList.add(history);
//            this.setHistories(historyList);
//        } else {
//            this.getHistories().add(history);
//        }
//        return this;
//    }

}
