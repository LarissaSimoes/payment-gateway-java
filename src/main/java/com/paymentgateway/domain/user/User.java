package com.paymentgateway.domain.user;

import com.paymentgateway.dtos.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name="users")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  @Column(unique = true)
  private String document;
  @Column(unique = true)
  private String email;
  private String password;
  private BigDecimal balance;
  @Enumerated(EnumType.STRING)
  private UserType userType;

  public User(UserDTO data) {
    this.firstName = data.firstName();
    this.lastName = data.lastName();
    this.balance = data.balance();
    this.userType = data.userType();
    this.password = data.password();
    this.email = data.email();
  }
}
