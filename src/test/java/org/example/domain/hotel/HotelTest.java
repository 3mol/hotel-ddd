package org.example.domain.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  @Test
  @DisplayName("酒店添加房间")
  void hotelAppendRoom() {
    // Given
    final Hotel hotel = new Hotel("1", "酒店", "地址", "电话", HotelStatus.OPEN, "");
    // When
    final Room room = new Room("1", RoomType.SINGLE, RoomStatus.FREE, 200, "401");
    hotel.appendRoom(room);
    // Then
    assertEquals(1, hotel.getRooms().size());
    assertEquals(RoomStatus.FREE, hotel.getRooms().get(0).getStatus());
    assertNotNull(hotel.getRooms().get(0).getRoomDoor());
    assertNotNull(hotel.getRooms().get(0).getRoomDoor().getRoomLock());
  }

  @Test
  @DisplayName("酒店重复添加房间，会抛出异常")
  void hotelAppendRoom2() {
    // Given
    final Hotel hotel = new Hotel("1", "酒店", "地址", "电话", HotelStatus.OPEN, "");
    // When
    final Room room = new Room("1", RoomType.SINGLE, RoomStatus.FREE, 200, "401");
    hotel.appendRoom(room);
    // Then
    assertThrows(RuntimeException.class, () -> hotel.appendRoom(room));
  }
}
