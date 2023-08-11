package com.example.demo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests.requestMatchers("/pointed-links").hasRole("USER")
                        .requestMatchers("/**").permitAll())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        Map<String, UserDetails> users = new HashMap<>();
        Map<String, String> registeredUsers = readUsersFromFile();

        for (String username : registeredUsers.keySet()) {
            String password = registeredUsers.get(username);
            users.put(username,
                    User.withDefaultPasswordEncoder()
                            .username(username)
                            .password(password)
                            .roles("USER")
                            .build());
        }

        return new InMemoryUserDetailsManager(users.values());
    }

    private Map<String, String> readUsersFromFile() {
        Map<String, String> registeredUsers = new HashMap<>();
        try {
            File currentDir = new File(".");
            File outputFile = new File(currentDir, "users.txt");
            FileReader reader = new FileReader(outputFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    registeredUsers.put(parts[0], parts[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registeredUsers;
    }
}