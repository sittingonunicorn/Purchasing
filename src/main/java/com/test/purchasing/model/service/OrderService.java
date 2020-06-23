package com.test.purchasing.model.service;

import com.test.purchasing.model.entity.Order;
import com.test.purchasing.model.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(Order order) {
        Order orderSaved = orderRepository.save(order);
        order.getItems().forEach(orderItem -> orderItem.setOrder(orderSaved));
        order.getItems().forEach(orderItemService::save);
    }

}
