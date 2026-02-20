package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            // These MUST be permitted for the callback to work
	            .requestMatchers("/login", "/loginUser", "/oauth2/**", "/register", "/adduser").permitAll()
	            .anyRequest().permitAll()
	        )
	        .oauth2Login(oauth -> oauth
	            .loginPage("/login")
	            .defaultSuccessUrl("/oauth2-success", true)
	            // This captures the failure instead of just showing ?error
	            .failureUrl("/login?error=true") 
	        );
	    return http.build();
	}
}