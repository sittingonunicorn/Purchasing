package com.test.purchasing.controller;

import com.test.purchasing.model.entity.Good;
import com.test.purchasing.model.entity.Order;
import com.test.purchasing.model.entity.OrderItem;
import com.test.purchasing.model.entity.User;
import com.test.purchasing.model.exception.GoodNotFoundException;
import com.test.purchasing.model.exception.NotEnoughMoneyException;
import com.test.purchasing.model.service.GoodService;
import com.test.purchasing.model.service.OrderService;
import com.test.purchasing.model.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Controller
@SessionAttributes({"order"})
public class OrderController {

    private final GoodService goodService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(GoodService goodService, OrderService orderService, UserService userService) {
        this.goodService = goodService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @ModelAttribute("order")
    private Order createOrder(@AuthenticationPrincipal User user) {
        return new Order(user, new ArrayList<>());
    }

    @GetMapping("/list")
    public String getListPage(Model model, @ModelAttribute("order") Order order) {
        model.addAttribute("balance", order.getUser().getBalance());
        model.addAttribute("goods", goodService.findAll());
        model.addAttribute("order_goods", order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList()));
        return "list.html";
    }

    @PostMapping("/list")
    public String addGood(Model model, @RequestParam(value = "goodId") Long goodId,
                          @ModelAttribute("order") Order order)
            throws GoodNotFoundException {
        order.addGoodToOrder(goodService.findById(goodId), order);
        model.addAttribute("goods", goodService.findAll());
        model.addAttribute("order_goods", order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList()));
        model.addAttribute("balance", order.getUser().getBalance());
        return "list.html";
    }

    @GetMapping("/pay")
    public String payPage(Model model, @ModelAttribute("order") Order order) {
        model.addAttribute("balance", order.getUser().getBalance());
        model.addAttribute("sum", order.getSum());
        return "pay.html";
    }

    @PostMapping("/pay/{goodId}")
    public String amountChange(Model model, @ModelAttribute("order") Order order,
                               @PathVariable String goodId, @RequestParam(value = "amount") Integer amount)
            throws GoodNotFoundException {
        model.addAttribute("balance", order.getUser().getBalance());
        Good good = goodService.findById(Long.parseLong(goodId));
        order.getItems().stream()
                .filter(orderItem -> orderItem.getGood().equals(good))
                .findFirst()
                .ifPresent(orderItem -> orderItem.setAmount(amount));
        model.addAttribute("sum", order.getSum());
        return "pay.html";
    }

    @PostMapping("/paid")
    @Transactional(rollbackFor = Exception.class)
    public String orderPayment(Model model, @ModelAttribute("order") Order order,
                               SessionStatus sessionStatus) throws NotEnoughMoneyException {
        BigDecimal balance = order.getUser().getBalance();
        BigDecimal sum = order.getSum();
        if (balance.compareTo(sum) < 0) {
            throw new NotEnoughMoneyException(balance, sum);
        }
        model.addAttribute("orderId", orderService.saveOrder(order));
        userService.subtractBalance(order.getUser(), sum);
        balance = order.getUser().getBalance();
        sessionStatus.setComplete();
        model.addAttribute("balance", balance);
        return "success.html";
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    String handleNotEnoughMoneyException(NotEnoughMoneyException e, Model model) {
        log.warn(e.getLocalizedMessage());
        model.addAttribute("error", true);
        return "redirect:/pay";
    }

    @ExceptionHandler(GoodNotFoundException.class)
    String handleGoodNotFoundException(GoodNotFoundException e, Model model) {
        log.warn(e.getLocalizedMessage());
        model.addAttribute("error", true);
        return "redirect:/list";
    }
}
