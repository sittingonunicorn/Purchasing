package com.test.purchasing.model.service;

import com.test.purchasing.model.entity.Good;
import com.test.purchasing.model.entity.Order;
import com.test.purchasing.model.entity.OrderItem;
import com.test.purchasing.model.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void addGoodToOrder(Good good, Order order) {
        List<OrderItem> items =  order.getItems();
        if (items.stream().map(OrderItem::getGood).noneMatch(g -> g.equals(good))){
            items.add(OrderItem.builder().good(good).amount(1).build());
            order.setItems(items);
        }
    }

//                    items.stream().filter(i->i.getGood().equals(good)).findAny().ifPresent(orderItem -> orderItem.setAmount(orderItem.getAmount()+amount))
}
