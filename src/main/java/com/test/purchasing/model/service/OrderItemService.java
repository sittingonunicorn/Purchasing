package com.test.purchasing.model.service;

import com.test.purchasing.model.entity.OrderItem;
import com.test.purchasing.model.repository.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void save(OrderItem item){
        orderItemRepository.save(item);
    }
}
