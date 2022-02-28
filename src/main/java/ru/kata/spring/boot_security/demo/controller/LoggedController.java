package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repo.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import ru.kata.spring.boot_security.demo.model.UserOperation;

@Controller
public class LoggedController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    /*@GetMapping("/admin2")
    public String testPageAdmin(Model model) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //model.addAttribute("allUsers", userService.getAllUsers());
        return "/admin2";
    }*/


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
        model.addAttribute("userid",userRepository.findByUsername(auth.getName()).getId());
        return "/admin";
    }

    // пришёл запрос на правку юзера
    /* @GetMapping("/admin/edituser/{id}")
   public String adminsEditPage(Model model, @PathVariable Optional<Long> id) {
        Long uid = null;
        if (id.isPresent()) {
            uid = id.get();
        }
        if (uid != null) {
            try {
                User user = userRepository.findById(uid).get();
                // чтобы параметр передался в форму, но не выводился в окне
                user.setPassword("");
                UserOperation uop = new UserOperation(user, "edituser");
                model.addAttribute("userOperData", uop);
            } catch (NoSuchElementException e) {
                model.addAttribute("processUserErrMsg", "Ошибка! Юзер для редактрования не найден - неверный id");
                model.addAttribute("userOperData", new UserOperation(new User(), ""));
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("myUserData",userRepository.findByUsername (auth.getName()));
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("newUserData", new UserOperation(new User(), "adduser"));
        return "/admin";
    }*/

    // пришёл запрос на удаление юзера
   /* @GetMapping("/admin/deleteuser/{id}")
    public String adminsDeletePage(Model model, @PathVariable Optional<Long> id) {
        Long uid = null;
        if (id.isPresent()) {
            uid = id.get();
        }
        if (uid != null) {
            try {
                User user = userRepository.findById(uid).get();
                UserOperation uop = new UserOperation(user, "deleteuser");
                model.addAttribute("userOperData", uop);
            } catch (NoSuchElementException e) {
                model.addAttribute("processUserErrMsg", "Ошибка! Юзер для удаления не найден - неверный id");
                model.addAttribute("userOperData", new UserOperation(new User(), ""));
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("myUserData",userRepository.findByUsername (auth.getName()));
        model.addAttribute("allUsers", userService.getAllUsers());

        model.addAttribute("newUserData", new UserOperation(new User(), "adduser"));
        return "/admin";
    }*/

    // обработчик CRUD
    /*@PostMapping("/admin")
    public String processUserOperation(ModelMap model, @ModelAttribute("userOperData") UserOperation uop, @ModelAttribute("newUserData") UserOperation uop2) {
        if ((uop != null) && (uop.getAction().equals("deleteuser"))) {
            try {
                userService.deleteUser(uop.getUser().getId());
                model.addAttribute("resultMsg","Пользователь "+uop.getUser().getUsername()+
                                    " ("+uop.getUser().getName()+" "+uop.getUser().getLastname()+") удалён");
            }
            catch (RuntimeException e) {
                model.addAttribute("processUserErrMsg","Не получилось удалить юзера "+uop.getUser().getUsername()+", почему - не знаю");
            }
        }
        //System.out.println("action:" + uop2.getAction());
        if ((uop2 != null) && (uop2.getAction().equals("adduser"))) {
            //System.out.println("пытаемся добавить нового юзера "+uop2.getUser().getUsername());
            if ((uop2.getUser().getPassword().equals(uop2.getUser().getPasswordConfirm())) && (!uop2.getUser().getPassword().equals(""))) {
                try {

                    userService.addUserMultiRole(uop2.getUser(),uop2.getRolesAsSet());
                    model.addAttribute("resultMsg","Пользователь "+uop2.getUser().getUsername()+ " добавлен");
                }
                catch (RuntimeException e) {
                    model.addAttribute("processUserErrMsg","Не получилось добавить пользователя, почему - не знаю");
                    System.out.println(e.getMessage());
                }
            } else {
                model.addAttribute("processUserErrMsg","Не получилось добавить пользователя: пароли не совпадают и/или пустые");
            }
        }
        if ((uop != null) && (uop.getAction().equals("edituser"))) {
            if (userService.editUser(uop.getUser(),uop.getRolesAsSet())) {
                model.addAttribute("resultMsg","Пользователь с id = "+uop.getUser().getId()+
                                    " (текущий юзернейм "+uop.getUser().getUsername()+") исправлен");
            } else {
                model.addAttribute("processUserErrMsg","Не получилось исправить пользоателя id = "+uop.getUser().getId()+
                                " (текущий юзернейм "+uop.getUser().getUsername()+"), почему не знаю");
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("myUserData",userRepository.findByUsername (auth.getName()));

        model.addAttribute("allUsers", userService.getAllUsers());

        //нужно для модальника - добвляем даже если нет операции
        model.addAttribute("userOperData", new UserOperation(new User(), ""));
        // чтобы поля на вкладке добавления были пустыми
        // userOperData не подходит т.к. может быть не пустым
        model.addAttribute("newUserData", new UserOperation(new User(), "adduser"));

        return "/admin";
    }*/

}
