package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

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

    public BigDecimal getLocalizedCost() {
        Locale locale = LocaleContextHolder.getLocale();
        BigDecimal itemPrice = good.getPrice().multiply(BigDecimal.valueOf(amount));
        return locale.equals(Locale.US) ? itemPrice.setScale(2, RoundingMode.HALF_EVEN)
                : itemPrice.multiply(BigDecimal.valueOf(26.7)).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getLocalizedDiscountCost() {
        Locale locale = LocaleContextHolder.getLocale();
        BigDecimal discountPrice =  useDiscount ? good.getDiscountPrice().multiply(BigDecimal.valueOf(amount))
                : getCost();
        return locale.equals(Locale.US) ? discountPrice.setScale(2, RoundingMode.HALF_EVEN)
                : discountPrice.multiply(BigDecimal.valueOf(26.7)).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getCost(){
        return good.getPrice().multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getDiscountCost(){
        return useDiscount? good.getDiscountPrice()
                .multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_EVEN) : getCost();
    }

    public BigDecimal getDiscountValue() {
        return useDiscount ? good.getPrice().subtract(good.getDiscountPrice())
                .multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.valueOf(0);
    }

    public BigDecimal getPossibleDiscount() {
        return good.getDiscount() != null ? good.getPrice().subtract(good.getDiscountPrice())
                .multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.valueOf(0);
    }
}
