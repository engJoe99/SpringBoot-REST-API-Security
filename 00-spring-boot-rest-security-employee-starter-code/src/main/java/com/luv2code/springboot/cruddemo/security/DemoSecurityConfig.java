package com.luv2code.springboot.cruddemo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


// Define the security configuration class
@Configuration
public class DemoSecurityConfig {


    // add support for JDBC ..... no more hard coded users
    // DataSource: A Spring-managed bean that provides access to the database
    // JdbcUserDetailsManager is initialized with the DataSource and loads the users from the database
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    // define 3 users, each user has a username, each user has a password,
    // each user has a set of roles
    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails user = User.builder().
                username("john").
                password("{noop}test123").
                roles("EMPLOYEE").
                build();

        UserDetails manager = User.builder().
                username("mary").
                password("{noop}test123").
                roles("Employee","MANAGER").
                build();

        UserDetails boss = User.builder().
                username("susan").
                password("{noop}test123").
                roles("EMPLOYEE", "MANAGER", "ADMIN").
                build();

        return new InMemoryUserDetailsManager(user, manager, boss);

        }

     */


    // define a security filter chain to map requests and http methods to specific roles
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
        );

        // use HTTP Basic Authentication
        http.httpBasic(Customizer.withDefaults());

        // disable Cross-Site Request Forgery (CSRF)
        // In general, not needed for statless REST APIs
        http.csrf( csrf -> csrf.disable());

        return http.build();
    }



}
