package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.InitService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class NoRegController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final InitService initService;

    @Autowired
    public NoRegController(UserService uS, InitService iS) {
        this.userService = uS;
        this.initService = iS;
        iS.initTestRolesAndUsers();
    }

    @GetMapping(value = "/")
    public String loginPage(ModelMap model, @RequestParam(defaultValue = "", required = false) String regsuccess) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        if (user != null) {
            System.out.println("Переход авторизованным на / не разрешён - редиректим на /user");
            return "redirect:/user";
        } else {
            System.out.println("Юзер не авторизован");
        }
        return "index";
    }
}