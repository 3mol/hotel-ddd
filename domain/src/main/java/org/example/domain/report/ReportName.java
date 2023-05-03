package org.example.domain.report;

public enum ReportName {
  BOOKED("预定报表"),
  CHECKED_IN("入住报表"), PAYMENT("收款报表");
  private final String name;

  ReportName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
