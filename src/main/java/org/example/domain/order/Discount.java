package org.example.domain.order;

public class Discount {
  private String name; // 折扣名
  private double discount; // 折扣

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }
}
