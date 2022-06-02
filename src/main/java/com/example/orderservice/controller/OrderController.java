package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.producer.KafkaOrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/order-service")
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final KafkaOrderProducer kafkaOrderProducer;

    @GetMapping("/health-check")
    public String check(){
        return "It's working";
    }

    @PostMapping("/{userId}/order")
    public ResponseOrder saveOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder){
        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        orderDto.setCreatedAt(LocalDateTime.now());
        orderDto.setTotalPrice(orderDto.calculateTotalPrice());
        orderDto.setOrderId(UUID.randomUUID().toString());

        OrderDto savedOrder = orderService.saveOrder(orderDto);

        kafkaOrderProducer.send("order-topic", orderDto);

        return modelMapper.map(savedOrder, ResponseOrder.class);
    }

    @GetMapping("{userId}/orders")
    public List<ResponseOrder> findOrder(@PathVariable("userId") String userId) {
        List<OrderDto> orders = orderService.getOrders(userId);
        List<ResponseOrder> result = new ArrayList<>();
        orders.forEach(order ->{
            ResponseOrder map = modelMapper.map(order, ResponseOrder.class);
            result.add(map);
        });

        return result;
    }

}
