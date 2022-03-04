package ru.kata.spring.boot_security.demo.repo;

import org.springframework.data.repository.CrudRepository;
import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleRepository extends CrudRepository<Role/*User*/, Long> {
}
