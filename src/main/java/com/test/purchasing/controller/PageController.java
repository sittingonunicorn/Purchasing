package com.test.purchasing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/list")
    public String getListPage() {
        return "list.html";
    }


    @GetMapping("/pay")
    public String getPayPage() {
        return "pay.html";
    }

    @GetMapping("/success")
    public String orderPayment() {
        return "success.html";
    }

    @GetMapping("/add_money")
    public String addMoney() {
        return "add_money.html";
    }
}
