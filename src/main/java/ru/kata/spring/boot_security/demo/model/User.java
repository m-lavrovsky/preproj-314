package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min=2, message = "<- Имя пользователя Не меньше 5 знаков")
    private String username;

    @Size(min=2, message = "<- Пароль не меньше 5 знаков")
    private String password;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String lastname;

    private Byte age;

    @Transient
    private String passwordConfirm;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @Transient
    private String roleStringJS;

    public User() {
    }

    public User(String name, String lastname, int age) {
        this.name = name;
        this.lastname = lastname;
        this.age = (byte) age;
    }

    public User(Long id, String name, String lastname, int age) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.age = (byte) age;
    }

    public User(Long id, String username, String name, String lastname, int age,
                String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.age = (byte) age;
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public String toString() {
        return "id " + getId()+" : " + getName() + " " + getLastname() + ", "+getAge()+" years old";
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getRolesAsString() {
        StringBuilder sb = new StringBuilder();
        int ctr = 0;
        if (this.roles.size() > 0) {
            for (Role role : this.roles) {
                if (ctr > 0) {
                    sb.append(", ");
                }
                sb.append(role.getNicename());
                ctr++;
            }
        } else {
            return "";
        }
        return sb.toString();
    }

    public String getRoleStringJS() {
        return roleStringJS;
    }

    public void setRoleStringJS(String roleStringJS) {
        this.roleStringJS = roleStringJS;
    }

    /*public boolean hasRole (String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) { return true; }
        }
        return false;
    }*/

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    // Переопределения с UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(String name) {
        for (Role role : this.roles) {
            if ((role.getNicename().equals(name)) || (role.getName().equals(name))){
                return true;
            }
        }
        return false;
    }
}


