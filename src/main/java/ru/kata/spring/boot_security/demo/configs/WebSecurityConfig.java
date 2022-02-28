package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSec) throws Exception {
        httpSec
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers("/","/register").not().fullyAuthenticated()
                .antMatchers("/","/register").anonymous() //Доступ только для не зарегистрированных пользователей:
                .antMatchers("/user*","/admin/myuser").fullyAuthenticated() // только авторизованные
                .antMatchers("/admin**").hasRole("ADMIN") //Доступ только для пользователей с ролью Администратор:
                //.antMatchers("/**").permitAll()
                //Настройка для входа в систему
                .and()
                .formLogin().successHandler(successUserHandler)
                .loginPage("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/dologout")
                .logoutSuccessUrl("/")
                .logoutRequestMatcher(new AntPathRequestMatcher("/dologout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }


    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}