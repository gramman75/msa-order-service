package com.example.orderservice.controller;

import com.example.orderservice.dto.KafkaOrderDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.producer.KafkaOrderJdbcProducer;
import com.example.orderservice.producer.KafkaOrderProducer;
import com.example.orderservice.schema.Field;
import com.example.orderservice.schema.OrderPayload;
import com.example.orderservice.schema.Schema;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// @RequestMapping("/order-service")
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final KafkaOrderProducer kafkaOrderProducer;
    private final KafkaOrderJdbcProducer kafkaOrderJdbcProducer;

    @GetMapping("/health-check")
    public String check(){
        return "It's working";
    }

    @PostMapping("/{userId}/order")
    public ResponseOrder saveOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder){

        log.info("user id:" + userId);
        System.out.println("user id ; " + userId);
        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        orderDto.setCreatedAt(LocalDateTime.now());
        orderDto.setTotalPrice(orderDto.calculateTotalPrice());
        orderDto.setOrderId(UUID.randomUUID().toString());

       OrderDto savedOrder = orderService.saveOrder(orderDto);





        kafkaOrderProducer.send("order-topic", orderDto);
        kafkaOrderJdbcProducer.send("orders", orderDto);

        return modelMapper.map(orderDto, ResponseOrder.class);
    }

    @GetMapping("{userId}/orders")
    public List<ResponseOrder> findOrder(@PathVariable("userId") String userId) {

        
        log.info("user id:" + userId);
        System.out.println("user id ; " + userId);

        List<OrderDto> orders = orderService.getOrders(userId);
        List<ResponseOrder> result = new ArrayList<>();
        orders.forEach(order ->{
            ResponseOrder map = modelMapper.map(order, ResponseOrder.class);
            result.add(map);
        });

        return result;
    }

}
