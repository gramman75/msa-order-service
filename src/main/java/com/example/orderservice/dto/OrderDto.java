package com.example.orderservice.dto;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class OrderDto {

    private Long id;

    private String productId;

    private Integer qty;

    private Integer unitPrice;

    private Integer totalPrice;

    private String userId;

    private String orderId;

    private Date createdAt;

    public Integer calculateTotalPrice(){
        return this.qty * this.unitPrice;
    }
}
