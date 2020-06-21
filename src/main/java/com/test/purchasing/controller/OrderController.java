package com.test.purchasing.controller;

import com.test.purchasing.model.entity.Order;
import com.test.purchasing.model.entity.OrderItem;
import com.test.purchasing.model.entity.User;
import com.test.purchasing.model.exception.GoodNotFoundException;
import com.test.purchasing.model.service.GoodService;
import com.test.purchasing.model.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Controller
@SessionAttributes({"order"})
public class OrderController {

    private final GoodService goodService;
    private final OrderService orderService;

    public OrderController(GoodService goodService, OrderService orderService) {
        this.goodService = goodService;
        this.orderService = orderService;
    }

    @ModelAttribute("order")
    private Order createOrder(@AuthenticationPrincipal User user) {
        return new Order(user, new ArrayList<>());
    }

    @GetMapping("/list")
    public String getListPage(Model model, @ModelAttribute("order") Order order) {
        model.addAttribute("goods", goodService.findAll());
        model.addAttribute("order_goods", order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList()));
        return "list.html";
    }

    @PostMapping("/list")
    public String addGood(Model model, @RequestParam(value = "goodId") Long goodId,
                          @ModelAttribute("order") Order order)
            throws GoodNotFoundException {
        orderService.addGoodToOrder(goodService.findById(goodId), order);
        model.addAttribute("goods", goodService.findAll());
        model.addAttribute("order_goods", order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList()));
        return "list.html";
    }

    @GetMapping("/pay")
    public String payPage(){
        return "pay.html";
    }

}
