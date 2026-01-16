package com.restaurant.ms.core.services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(String.format("User Not Found with username: %s", username));
    }

    boolean enabled = true;
    boolean accountNonExpired = true;
    boolean credentailsNonExpired = true;
    boolean accountNonLocked = true;

    return new org.springframework.security.core.userdetails.User(
      username,
      user.getPassword(),
      enabled,
      accountNonExpired,
      credentailsNonExpired,
      accountNonLocked,
        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
    );
  }
}
