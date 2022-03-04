package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class InitService {
    @Autowired
    private final UserService userService;

    @Autowired
    private final RoleService roleService;

    @Autowired
    public InitService(UserService uS, RoleService rS) {
        this.userService = uS;
        this.roleService = rS;
    }

    @Transactional
    public void initTestRolesAndUsers() {
        roleService.addRole(new Role(1L,"ROLE_ADMIN","админ"));
        roleService.addRole(new Role(2L,"ROLE_USER","пользователь"));

        // тестовый админ: логин = admin, пароль = test
        userService.addUser(new User(0L, "admin", "Админ", "Админов", 10, "test"),"admin");
        // тестовый юзер: логин = user, пароль = test
        userService.addUser(new User(0L, "user", "Пользователь", "Юзеров", 52, "test"),"user");
        userService.addUser(new User(0L, "harrypotter30", "Гаррисон", "Поттер", 30, "test"),"user");
        userService.addUser(new User(0L, "dartwader99", "Дарт", "Вейдер", 99, "test"),"user");
        Set<Role> roles5 = new HashSet<>();
        roles5.add(new Role(1L));
        roles5.add(new Role(2L));
        userService.addUserMultiRole(new User(0L, "bilbo111", "Бильбо", "Беггинс", 111, "test"),roles5);
    }
}
