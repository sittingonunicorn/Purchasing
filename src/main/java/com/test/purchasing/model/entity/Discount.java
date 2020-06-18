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
@Table(name = "discount",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"percent", "discount_id"})})
public class Discount {

    @Id
    @GeneratedValue
    @Column(name = "discount_id", nullable = false)
    private Long id;

    @Column(name = "percent", nullable = false)
    private Integer percent;
}
