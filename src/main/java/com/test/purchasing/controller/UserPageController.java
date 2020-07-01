package com.test.purchasing.controller;

import com.test.purchasing.model.dto.UserRegistrationDTO;
import com.test.purchasing.model.entity.User;
import com.test.purchasing.model.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Controller
public class UserPageController {

    private final MessageSource messageSource;
    private final UserService userService;

    @Autowired
    public UserPageController(MessageSource messageSource, UserService userService) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @GetMapping({"/index", "/"})
    public String indexPage() {
        return "index.html";
    }

    @GetMapping("/main")
    public String mainPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("balance", user.getLocalizedBalance());
        return "main.html";
    }

    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login.html";
    }

    @GetMapping("/create_user")
    public String userCreation(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "create_user.html";
    }

    @GetMapping("/get_user")
    public @ResponseBody String getUser(@AuthenticationPrincipal User user) {
        return user.getName();
    }

    @PostMapping("/create_user")
    public String addUser(@Valid @ModelAttribute UserRegistrationDTO userRegistrationDTO, Model model,
                          Locale locale) {
        try {
            userService.saveNewUser(userRegistrationDTO);
            log.info("User " + userRegistrationDTO.getEmail() + " is successfully registered.");
        } catch (DataAccessException | ValidationException e) {
            log.info(e.getLocalizedMessage());
            model.addAttribute("emailError", messageSource.getMessage("email.not.unique", null, locale));
            return "create_user.html";
        }
        return "redirect:/login";
    }

    @PostMapping("/replenish_balance")
    @ResponseBody
    public void replenishBalance(@RequestBody String sum, @AuthenticationPrincipal User user) {
        userService.replenishBalance(user, Integer.parseInt(sum.substring(0, sum.indexOf('='))));
    }

    @GetMapping("/balance")
    public @ResponseBody
    BigDecimal getBalance(@AuthenticationPrincipal User user) {
        return user.getLocalizedBalance().setScale(2, RoundingMode.HALF_EVEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public String handleValidationExceptions(BindException ex, Locale locale,
                                             Model model) {
        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.add(messageSource.getMessage(error.getDefaultMessage(), null, locale)));
        model.addAttribute("errors", errors);
        return "create_user.html";
    }

}