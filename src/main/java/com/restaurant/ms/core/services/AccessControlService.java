package com.restaurant.ms.core.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.restaurant.ms.core.exceptions.GeneralException;
import com.restaurant.ms.core.models.Outlet;
import com.restaurant.ms.core.models.User;

import lombok.RequiredArgsConstructor;

@Service("accessControl")
@RequiredArgsConstructor
public class AccessControlService {
  private User getPrincipal(Authentication auth) {
    User current = (User) auth.getPrincipal();

    if (current == null) {
      throw new GeneralException("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    return current;
  }

  public boolean isOwner(Authentication auth, UUID ownerId) {
    User user = getPrincipal(auth);
    return user.getId().equals(ownerId);
  }

  public boolean sameOutlet(Authentication auth, Outlet outlet) {
    User current = getPrincipal(auth);

    return current.getOutlet().equals(outlet);
  }

  public boolean hasAccess() {
    return false;
  }

  public boolean checkPermission() {
    return false;
  }
}
