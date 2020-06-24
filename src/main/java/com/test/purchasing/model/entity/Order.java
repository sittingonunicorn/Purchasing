package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"order_id"})})
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<OrderItem> items;

    public Order(User user, List<OrderItem> items) {
        this.user = user;
        this.items = items;
    }

    public BigDecimal getSum() {
        Locale locale = LocaleContextHolder.getLocale();
        BigDecimal sum = new BigDecimal(0);
        setOptionalDiscount();
        for (OrderItem item : items
        ) {
            sum = sum.add(item.getDiscountCost());
        }
        return locale.equals(Locale.US) ? sum : sum.multiply(BigDecimal.valueOf(26.7));
    }

    private void setOptionalDiscount() {
        if (items.stream().map(item -> item.getGood().getDiscount()).filter(Objects::nonNull).count() > 3) {
            items.sort(Comparator.comparing(OrderItem::getPossibleDiscount).reversed());
            items.stream()
                    .skip(3L).forEach(orderItem -> orderItem.setUseDiscount(false));
            items.stream()
                    .limit(3L).forEach(orderItem -> orderItem.setUseDiscount(true));
        }
    }

    public void addGoodToOrder(Good good) {
        if (items.stream().map(OrderItem::getGood).noneMatch(g -> g.equals(good))) {
            items.add(OrderItem.builder().good(good).amount(1).useDiscount(true).build());
        }
    }

    public void deleteOrderItem(Good good) {
        items.stream().filter(orderItem -> orderItem.getGood().equals(good)).findFirst().ifPresent(items::remove);
    }

    public void changeAmount(Good good, int amount){
        items.stream()
                .filter(orderItem -> orderItem.getGood().equals(good))
                .findFirst()
                .ifPresent(orderItem -> orderItem.setAmount(amount));
    }

}
