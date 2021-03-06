package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class RESTController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        //return userService.getAllUsers();
        List<User> userList = userService.getAllUsers();
        return (!userList.isEmpty())
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        User user = userService.findUserByIdOrNull(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/myuser")
    public ResponseEntity<User> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.addUser(user);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

   @PutMapping("/users")
    public String editUser(@RequestBody User user) {
       System.out.println("starting user change");
       if (userService.editUser(user)) {
           System.out.println("user");
           return "user had been changed successfully";
       } else {
           System.out.println("no user");
           return "user had not been changed successfully";
       }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        if (userService.deleteUser(id)) {
            return "user has been deleted";
        } else {
            return "user has not been deleted";
        }
    }
}
