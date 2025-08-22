package org.example.xphrtestingassignment.config;

import org.example.xphrtestingassignment.constant.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/web/reports")
                        .hasAnyRole(UserRoles.EMPLOYEE.name(), UserRoles.ADMIN.name())
                        .anyRequest()
                        .permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin").password(passwordEncoder.encode("admin"))
                .roles(UserRoles.ADMIN.name()).build();
        UserDetails tom = User.builder()
                .username("tom").password(passwordEncoder.encode("tom"))
                .roles(UserRoles.EMPLOYEE.name()).build();
        UserDetails jerry = User.builder()
                .username("jerry").password(passwordEncoder.encode("jerry"))
                .roles(UserRoles.EMPLOYEE.name()).build();
        return new InMemoryUserDetailsManager(tom, jerry, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
