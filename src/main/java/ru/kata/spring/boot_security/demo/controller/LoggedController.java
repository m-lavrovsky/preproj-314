package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class LoggedController {
    @Autowired
    private UserService userService;

    @GetMapping(value="/user")
    public String userPage(){
        return "/user";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "/error";
    }

    @GetMapping("/admin")
    public String adminsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userid",userService.findUserByUsername(auth.getName()).getId());
        return "/admin";
    }

}
