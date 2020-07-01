package com.test.purchasing.controller;

import com.test.purchasing.model.dto.CreateOrderDTO;
import com.test.purchasing.model.entity.Good;
import com.test.purchasing.model.entity.OrderItem;
import com.test.purchasing.model.entity.User;
import com.test.purchasing.model.exception.GoodNotFoundException;
import com.test.purchasing.model.exception.NotEnoughMoneyException;
import com.test.purchasing.model.service.GoodService;
import com.test.purchasing.model.service.OrderService;
import com.test.purchasing.model.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SessionAttributes({"order"})
public class OrderController{

    private final GoodService goodService;
    private final OrderService orderService;
    private final UserService userService;
    private final MessageSource messageSource;

    public OrderController(GoodService goodService, OrderService orderService, UserService userService,
                           MessageSource messageSource) {
        this.goodService = goodService;
        this.orderService = orderService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @ModelAttribute("order")
    private CreateOrderDTO createOrder(@AuthenticationPrincipal User user) {
        return new CreateOrderDTO(user, new ArrayList<>());
    }

    @GetMapping("/goods")
    public List<Good> getGoodsList() {
        return goodService.findAll();
    }

    @GetMapping("/order_goods")
    public List<Good> getOrderGoodsList(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList());
    }

    @PostMapping("/list/add")
    public void addGood(@RequestBody Good good, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        order.addGoodToOrder(good);
        log.info("Good with id: " + good.getId() + " is added to order");
    }

    @GetMapping("/items_list")
    public List<OrderItem> getItemsList(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getItems();
    }

    @GetMapping("/sum")
    public BigDecimal getSum(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getSum();
    }

    @GetMapping("/equals")
    public Boolean getGoodId(Good good, Good good2){
        System.out.println(good2.equals(good));
        return good2.equals(good);
    }

    @PostMapping("/amount/{amount}")
    public void setAmount(@RequestBody Good good, @PathVariable Integer amount, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        order.changeAmount(good, amount);
    }

    @PostMapping("/delete_item")
    public void deleteGood(@RequestBody Good good, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        order.deleteOrderItem(good);
    }

    @PostMapping("/paid")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, String>> orderPayment(Model model, SessionStatus sessionStatus) throws NotEnoughMoneyException {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        BigDecimal balance = order.getUser().getLocalizedBalance();
        BigDecimal sum = order.getSum();
        if (balance.compareTo(sum) < 0) {
            throw new NotEnoughMoneyException(balance, sum);
        }
        orderService.saveOrder(order);
        userService.subtractBalance(order.getUser(), sum);
        sessionStatus.setComplete();
        return ResponseEntity.ok().body(
                Collections.singletonMap("message",
                        messageSource.getMessage("message.order.paid", null,
                                LocaleContextHolder.getLocale())));
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
