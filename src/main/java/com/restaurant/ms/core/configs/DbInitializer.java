package com.restaurant.ms.core.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.restaurant.ms.auth.enums.EAuthProvider;
import com.restaurant.ms.core.enums.EPermission;
import com.restaurant.ms.core.models.Address;
import com.restaurant.ms.core.models.Admin;
import com.restaurant.ms.core.models.Outlet;
import com.restaurant.ms.core.models.Permission;
import com.restaurant.ms.core.models.Role;
import com.restaurant.ms.core.models.User;
import com.restaurant.ms.core.repositories.AdminRepository;
import com.restaurant.ms.core.repositories.OutletRepository;
import com.restaurant.ms.core.repositories.PermissionRepository;
import com.restaurant.ms.core.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DbInitializer {
  private final PasswordEncoder passwordEncoder;
  private final PermissionRepository permissionRepository;
  private final RoleRepository roleRepository;
  private final AdminRepository adminRepository;
  private final OutletRepository outletRepository;

  private Map<String, Role> roles = new HashMap<>();
  private Outlet outlet = null;
  private Map<String, User> accounts = new HashMap<>();

  @Bean
  CommandLineRunner init() {
    return args -> {
      initOutlet();
      initPermissions();
      initRoles();
      initUsers();
    };
  }

  private Address getAddress() {
    Address address = new Address();

    address.setCity("Scarborough");
    address.setCountry("Canada");
    address.setState("Ontario");
    address.setStreet("29 Lord Robert Drive");
    address.setZipCode("M1K 4C2");

    return address;
  }

  private void initPermissions() {
    for (EPermission permission : EPermission.values()) {
      if (!permissionRepository.existsByName(permission)) {
        //
      }
    }
  }

  private void initOutlet() {
    Outlet newOutlet = new Outlet();

    newOutlet.setAddress(getAddress());
    newOutlet.setTel("987654321");

    this.outlet = outletRepository.save(newOutlet);
  }

  private Role createRole(String name, Set<Permission> permissions, Integer level) {
    Role role = new Role();

    role.setName(name);
    role.setPermissions(permissions);
    role.setLevel(level);

    return roleRepository.save(role);
  }

  private void initRoles() {
    Role superAdminRole = createRole("Super Admin", null, 1);
    Role customerRole = createRole("Customer", null, 2);

    roles.put("SUPER_ADMIN", superAdminRole);
    roles.put("CUSTOMER", customerRole);
  }

  private void initUsers() {
    Admin superAdmin = createAdmin(
        "shresthaheriz15@gmail.com",
        "Test",
        "Super Admin",
        "superadmin",
        "SuperAdmin@123",
        roles.get("SUPER_ADMIN"),
        EAuthProvider.LOCAL,
        true,
        true,
        outlet);
    accounts.put("superAdmin", adminRepository.save(superAdmin));

  }

  private Admin createAdmin(String email, String firstname, String lastname, String username, String password,
      Role role, EAuthProvider authProvider, boolean isVerifiedEmail, boolean isEnabled, Outlet outlet) {
    Admin admin = new Admin();

    admin.setEmail(email);
    admin.setFirstName(firstname);
    admin.setLastName(lastname);
    admin.setUsername(username);
    admin.setPassword(passwordEncoder.encode(password));
    admin.setRole(role);
    admin.setAuthProvider(authProvider);
    admin.setVerifiedEmail(isVerifiedEmail);
    admin.setEnabled(isEnabled);
    admin.setOutlet(outlet);

    return admin;
  }
}
