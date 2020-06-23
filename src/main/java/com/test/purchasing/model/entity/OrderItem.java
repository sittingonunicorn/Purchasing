package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_item",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"order_item_id"})})
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "good_id", nullable = false)
    private Good good;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "useDiscount")
    private boolean useDiscount;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem(Good good, Integer amount) {
        this.good = good;
        this.amount = amount;
        useDiscount = true;
    }

    public BigDecimal getCost(){
        return good.getPrice().multiply(new BigDecimal(amount));
    }

    public BigDecimal getDiscountCost(){
        return useDiscount? good.getDiscountPrice().multiply(new BigDecimal(amount)) : getCost();
    }

    public BigDecimal getDiscount(){
        return useDiscount? good.getPrice().subtract(good.getDiscountPrice())
                .multiply(new BigDecimal(amount)) : new BigDecimal(0);
    }

    public BigDecimal getPossibleDiscount() {
        return good.getDiscount()!=null ? good.getPrice().subtract(good.getDiscountPrice())
                .multiply(new BigDecimal(amount)) : new BigDecimal(0);
    }
}
