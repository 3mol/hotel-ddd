package org.example.client.user;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class Customer {
  private String name;
  private String idCard;
  private String phone;

  public Customer(String name, String idCard, String phone) {
    this.name = name;
    this.idCard = idCard;
    this.phone = phone;
  }

  public Customer() {}
}
