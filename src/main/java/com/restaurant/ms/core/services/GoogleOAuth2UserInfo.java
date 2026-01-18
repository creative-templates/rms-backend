package com.restaurant.ms.core.services;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import com.restaurant.ms.core.models.User;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
  private final OAuth2AuthenticatedPrincipal principal;

  public GoogleOAuth2UserInfo(OAuth2AuthenticatedPrincipal principal) {
    this.principal = principal;
  }

  @Override
  public String getEmail() {
    return principal.getAttribute("email");
  }

  @Override
  public String getFirstName() {
    return principal.getAttribute("given_name");
  }

  @Override
  public String getLastName() {
    return principal.getAttribute("family_name");
  }

  @Override
  public String getProfilePicture() {
    return principal.getAttribute("picture");
  }

  @Override
  public User getUser() {
    User user = new User();

    user.setEmail(principal.getAttribute("email"));
    user.setFirstName(principal.getAttribute("given_name"));
    user.setLastName(principal.getAttribute("family_name"));
    user.setProfilePictire(principal.getAttribute("picture"));

    return user;
  }

}
