package org.example.domain.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.domain.order.Order;
import org.example.domain.order.Pay;
import org.example.domain.order.PayFactory;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.domain.user.Customer;
import org.junit.jupiter.api.Assertions;
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

  @Test
  @DisplayName("预定房间, 但是未支付")
  void hotelReserveRoom() {
    // Given
    final Hotel hotel = new Hotel("1", "酒店", "地址", "电话", HotelStatus.OPEN, "");
    final Room room = new Room("1", RoomType.SINGLE, RoomStatus.FREE, 200, "401");
    hotel.appendRoom(room);
    // When
    final Customer customer = new Customer("小明", "身份证", "小明电话");
    Order order = hotel.reserveRoom(room.getRoomDoor(), customer);
    // Then
    final Stream<Pay> payStream =
        order.getPays().stream().filter(i -> i.getStatus() == PayStatus.UNPAID);
    final List<Pay> payList = payStream.collect(Collectors.toList());
    Assertions.assertEquals(3, payList.size());
    Assertions.assertEquals(
        room.getPrice() * PayFactory.DEPOSIT_PROPORTION, getPay(payList, PayType.DEPOSIT));
    Assertions.assertEquals(
        room.getPrice() * PayFactory.FINAL_PAYMENT_PROPORTION,
        getPay(payList, PayType.FINAL_PAYMENT));
    Assertions.assertEquals(PayFactory.DEPOSIT_CHARGE, getPay(payList, PayType.DEPOSIT_CHARGE));
  }

  private static Double getPay(List<Pay> payList, PayType payType) {
    return payList.stream()
        .filter(i -> i.getType() == payType)
        .map(Pay::getAmount)
        .findAny()
        .orElseThrow();
  }
}
