package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderDto saveOrder(OrderDto orderDto){
        OrderEntity map = modelMapper.map(orderDto, OrderEntity.class);
        OrderEntity saveEntity = orderRepository.save(map);
        OrderDto result = modelMapper.map(saveEntity, OrderDto.class);
        return result;
    }

    public List<OrderDto> getOrders(String userId){

        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
        List<OrderDto> result = new ArrayList<>();
        orderEntities.forEach(order ->{
            OrderDto map = modelMapper.map(order, OrderDto.class);
            result.add(map);
        });

        return result;
    }

}
