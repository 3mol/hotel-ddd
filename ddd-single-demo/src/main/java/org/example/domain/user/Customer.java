package org.example.domain.user;

import lombok.Data;

@Data
public class Customer {
  private String name;
  private String idCard;
  private String phone;

  public Customer(String name, String idCard, String phone) {
    this.name = name;
    this.idCard = idCard;
    this.phone = phone;
  }
}
