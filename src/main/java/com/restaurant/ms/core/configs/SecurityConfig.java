package com.restaurant.ms.core.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;
import com.restaurant.ms.core.roles.ERole;
import com.restaurant.ms.core.services.JwtAuthenticationEntryPoint;
import com.restaurant.ms.core.services.JwtAuthenticationFilter;
import com.restaurant.ms.core.services.MyUserDetailsService;
import com.restaurant.ms.core.services.OAuth2SuccessHandler;
import com.restaurant.ms.core.services.OAuth2UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final MyUserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final OAuth2UserServiceImpl oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final UserRepository userRepository;

  @Profile("dev")
  @Bean
  public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    User superAdmin = new User();
    superAdmin.setEmail("shresthaheriz15@gmail.com");
    superAdmin.setFirstName("Test");
    superAdmin.setLastName("Super");
    superAdmin.setUsername("superadmin");
    superAdmin.setPassword(passwordEncoder().encode("SuperAdmin@123"));
    superAdmin.setRole(ERole.SUPER_ADMIN);
    superAdmin.setVerifiedEmail(true);
    superAdmin.setEnabled(true);

    userRepository.save(superAdmin);

    return new InMemoryUserDetailsManager(superAdmin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .authenticationProvider(authenticationProvider())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .authorizeHttpRequests(
            api -> api
                .requestMatchers(
                    "/api/auth/login",
                        "/api/auth/activate-account",
                    "/api/auth/forgot-password/**",
                    "/oauth2/**")
                .permitAll()
                .anyRequest().authenticated())
        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(u -> u.userService(oAuth2UserService))
            .successHandler(oAuth2SuccessHandler))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
