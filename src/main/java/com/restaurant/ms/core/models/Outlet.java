package com.restaurant.ms.core.models;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "outlets")
@Data
public class Outlet {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String tel;

  @Embedded
  private Address address;
}
