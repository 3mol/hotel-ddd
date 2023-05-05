package org.example.base;

import javax.persistence.Embeddable;

@Embeddable
public class Discount {
  private String discountName; // 折扣名
  private Double discount; // 折扣

  public String getDiscountName() {
    return discountName;
  }

  public void setDiscountName(String name) {
    this.discountName = name;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }
}
