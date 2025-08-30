package com.samsung3.quangbatourdulich.config;

import com.samsung3.quangbatourdulich.config.Jwt.JwtEntryPoint;
import com.samsung3.quangbatourdulich.config.Jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/tours/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/tours/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tours/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tours/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/bookings/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/blogposts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/blogposts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/blogposts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/blogposts/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}