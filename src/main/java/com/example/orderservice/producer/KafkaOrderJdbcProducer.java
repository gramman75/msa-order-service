package com.example.orderservice.producer;

import com.example.orderservice.dto.KafkaOrderDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.schema.Field;
import com.example.orderservice.schema.OrderPayload;
import com.example.orderservice.schema.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderJdbcProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    List<Field> fields = Arrays.asList(
        new Field("string", true, "order_id"),
        new Field("string", true, "user_id"),
        new Field("string", true, "product_id"),
        new Field("int32", true, "qty"),
        new Field("int32", true, "total_price"),
        new Field("int32", true, "unit_price")
    );
    Schema schema = Schema.builder()
                .type("struct")
                .fields(fields)
                .optional(false)
                .name("orders")
                .build();


    public OrderDto send(String topic, OrderDto orderDto){

        OrderPayload orderPayload = OrderPayload.builder()
                .order_id(orderDto.getOrderId())
                .user_id(orderDto.getUserId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .total_price(orderDto.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, orderPayload);
//        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(orderPayload);
//
//
        String kafkaOrderDtoStr = "";

        try {
            kafkaOrderDtoStr = objectMapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("kafkaOrderDtoStr ===>" + kafkaOrderDtoStr);

        kafkaTemplate.send(topic, kafkaOrderDtoStr);

        return orderDto;

    }
}
