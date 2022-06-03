package com.example.orderservice.dto;

import com.example.orderservice.schema.OrderPayload;
import com.example.orderservice.schema.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private OrderPayload payload;
}
