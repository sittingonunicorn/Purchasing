package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> items;
}
