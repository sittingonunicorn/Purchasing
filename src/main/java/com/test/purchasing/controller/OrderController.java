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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SessionAttributes({"order"})
public class OrderController {

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
    public List<Good> getGoodsList(@ModelAttribute("order") CreateOrderDTO order) {
        return goodService.findAll().stream()
                .filter(good -> getOrderGoodsList(order).stream()
                        .noneMatch(g-> good.getId().equals(g.getId()))).collect(Collectors.toList());
    }

    @GetMapping("/order_goods")
    public List<Good> getOrderGoodsList(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getItems().stream()
                .map(OrderItem::getGood).collect(Collectors.toList());
    }

    @PostMapping("/list/add")
    public List<Good> addGood(@RequestBody Good good, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        order.addGoodToOrder(good);
        log.info("Good with id: " + good.getId() + " is added to order");
        return getGoodsList(order);
    }

    @GetMapping("/items_list")
    public List<OrderItem> getItemsList(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getItems();
    }

    @GetMapping("/sum")
    public BigDecimal getSum(@ModelAttribute("order") CreateOrderDTO order) {
        return order.getSum();
    }

    @PostMapping("/amount/{amount}")
    public void setAmount(@RequestBody Good good, @PathVariable Integer amount, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        assert order != null;
        order.changeAmount(good, amount);
    }

    @PostMapping("/delete_item")
    public void deleteGood(@RequestBody Good good, Model model) {
        CreateOrderDTO order = (CreateOrderDTO) model.getAttribute("order");
        assert order != null;
        order.deleteOrderItem(good);
    }

    @PostMapping("/paid")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, String>> orderPayment(Model model, SessionStatus sessionStatus)
            throws NotEnoughMoneyException {
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

    @ExceptionHandler({NotEnoughMoneyException.class, GoodNotFoundException.class})
    public final ResponseEntity<Map<String, String>> handleCustomExceptions(Exception ex) {
        return ResponseEntity.badRequest().body(
                Collections.singletonMap("error", ex.getLocalizedMessage()));
    }
}
