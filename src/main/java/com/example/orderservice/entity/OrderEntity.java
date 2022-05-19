package com.example.orderservice.entity;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String orderId;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
}
