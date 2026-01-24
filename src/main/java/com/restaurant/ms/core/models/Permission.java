package com.restaurant.ms.core.models;

import java.util.UUID;

import com.restaurant.ms.core.enums.EPermission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions", uniqueConstraints = {@UniqueConstraint(columnNames = {"related_to", "action"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "related_to")
  private String relatedTo;

  private String action;

  @Enumerated(EnumType.STRING)
  @Column(unique = true, nullable = false)
  private EPermission name;
}
