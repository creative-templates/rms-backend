package com.restaurant.ms.core.services;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserRepository userRepository;
  private final TokenService tokenService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();

    OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(principal);

    User user = userRepository.findByUsername(userInfo.getEmail())
        .orElseGet(() -> {
          try {
            return userRepository.save(userInfo.getUser());
          } catch (TransactionSystemException ex) {
            throw new GeneralException(ex.getMessage());
          } catch (Exception ex) {
            throw new GeneralException(ex.getMessage());
          }
        });

    response.addHeader("Set-Cookie", tokenService.getRefreshCookie(user));

    response.sendRedirect("http://localhost:3000/oauth-success");
  }
}
