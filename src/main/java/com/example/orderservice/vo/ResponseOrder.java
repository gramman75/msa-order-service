package com.example.orderservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseOrder {
    private Long id;

    private String productId;

    private Integer qty;

    private Integer unitPrice;

    private Integer totalPrice;

    private String userId;

    private String orderId;

    private Date createdAt;
}
