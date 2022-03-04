package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;
import ru.kata.spring.boot_security.demo.repo.UserRepository;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return user;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(new User());
    }

    public User findUserByIdOrNull(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(user -> {result.add(user);});
        return result;
    }


    public boolean editUser(User user) {
        Iterable<Role> dbRoles = roleRepository.findAll();

        Set<Role> roles = new HashSet<>();

        User userFromDB = findUserByIdOrNull(user.getId());
        if (userFromDB != null) {
            for (Role role : dbRoles) {
                if (user.getRoleStringJS().contains(role.getName())) {
                    roles.add(new Role (role.getId(),role.getName(),role.getNicename()));
                }
            }
            if (roles.isEmpty()) { roles.add(new Role(2L,"ROLE_USER","пользователь")); }
            user.setRoles(roles);

            if (!user.getPassword().equals("")) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(userFromDB.getPassword());
            }

            userRepository.save(user);
            return true;
        }
        return false;
    }

    // добавляет пользоателя с правами по умолчанию (юзер)
    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        if (user.getPassword().isEmpty()) {
            return false;
        }
        Iterable<Role> dbRoles = roleRepository.findAll();
        Set<Role> roles = new HashSet<>();
        for (Role role : dbRoles) {
            if (user.getRoleStringJS().contains(role.getName())) {
                roles.add(new Role (role.getId(),role.getName(),role.getNicename()));
            }
        }
        if (roles.isEmpty()) { roles.add(new Role(2L,"ROLE_USER","пользователь")); }
        user.setRoles(roles);
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // добавляет юзера с правами по роли указанной человекопонятным языком, добавлено для инициализации
    public boolean addUser(User user, String role) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        if (role.equals("admin")) {
            user.setRoles(Collections.singleton(new Role(1L)));
        }
        if (role.equals("user")) {
            user.setRoles(Collections.singleton(new Role(2L)));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    // добавляет юзера с набором прав (можно и одно)
    public boolean addUserMultiRole(User user, Set<Role> roles) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setRoles(roles);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

}
