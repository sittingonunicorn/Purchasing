package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
