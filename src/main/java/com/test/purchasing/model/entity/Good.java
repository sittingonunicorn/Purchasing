package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "good",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "good_id"})})
public class Good {

    @Id
    @GeneratedValue
    @Column(name = "good_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "discount_id")
    private Discount discount;


    public BigDecimal getDiscountPrice() {
        if (discount == null) {
            return price;
        } else {
            BigDecimal discountPercent = new BigDecimal(discount.getPercent());
            BigDecimal hundred = new BigDecimal(100);
            return price.multiply((hundred.subtract(discountPercent).divide(hundred, 2, RoundingMode.HALF_EVEN)));
        }
    }
}
