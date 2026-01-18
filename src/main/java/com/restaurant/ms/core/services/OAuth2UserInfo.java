package com.restaurant.ms.core.services;

import com.restaurant.ms.core.models.User;

public interface OAuth2UserInfo {
  String getEmail();
  String getFirstName();
  String getLastName();
  String getProfilePicture();
  User getUser();
}
