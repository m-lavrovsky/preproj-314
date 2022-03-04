package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;


@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public boolean addRole(Role role) {
        roleRepository.save(role);
        return true;
    }
}
