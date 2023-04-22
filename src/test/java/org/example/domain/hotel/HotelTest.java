package org.example.domain.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HotelTest {
  @Test
  @DisplayName("服务员入职酒店")
  void appendEmployee() {
    // Given
    final Hotel hotel = new Hotel("1", "酒店", "地址", "电话", HotelStatus.OPEN, "");
    // When
    final Employee employee = new Employee("1", "小红", "小红电话");
    hotel.employeeOnboarding(employee, RoleType.WAITER);
    // Then
    assertEquals(1, hotel.getEmployees().size());
    final Employee exceptionValue =
        new Employee("1", "小红", "小红电话", EmployeeStatus.ON_JOB, RoleType.WAITER);
    assertEquals(exceptionValue, hotel.getEmployees().get(0));
  }

  @Test
  @DisplayName("服务员入职酒店后离职")
  void employeeTurnover() {
    // Given
    final Hotel hotel = new Hotel("1", "酒店", "地址", "电话", HotelStatus.OPEN, "");
    final Employee employee = new Employee("1", "小红", "小红电话");
    hotel.employeeOnboarding(employee, RoleType.WAITER);
    // When
    hotel.employeeTurnover(employee);
    // Then
    assertEquals(1, hotel.getEmployees().size());
    final Employee exceptionValue =
        new Employee("1", "小红", "小红电话", EmployeeStatus.OFF_JOB, RoleType.WAITER);
    assertEquals(exceptionValue, hotel.getEmployees().get(0));
  }
}
