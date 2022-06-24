package com.jandex.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "category_history")
public class CategoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_parent", nullable = false, foreignKey = @ForeignKey(name = "fk_history_category"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "price", nullable = false)
    private Long price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryHistory history = (CategoryHistory) o;
        return id.equals(history.id) && date.equals(history.date) && price.equals(history.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, price);
    }
}
