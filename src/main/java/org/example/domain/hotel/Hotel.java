package org.example.domain.hotel;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.domain.order.Order;
import org.example.domain.order.OrderFactory;
import org.example.domain.order.Pay;
import org.example.domain.user.Customer;

@Data
@AllArgsConstructor
public class Hotel {
  private String id;
  private String name;
  private String address;
  private String phone;
  private HotelStatus status;
  private String description;
  private List<Room> rooms;
  private List<Employee> employees;
  private List<Order> reserves;
  private List<Pay> pays;
  private List<Order> orders;

  public Hotel(
      String id,
      String name,
      String address,
      String phone,
      HotelStatus status,
      String description) {
    this(
        id,
        name,
        address,
        phone,
        status,
        description,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public void employeeOnboarding(Employee employee, RoleType roleType) {
    employee.setStatus(EmployeeStatus.ON_JOB);
    employee.setRole(roleType);
    this.employees.add(employee);
  }

  public void employeeTurnover(Employee employee) {
    this.employees.stream()
        .filter(i -> i.equals(employee))
        .findFirst()
        .orElseThrow()
        .setStatus(EmployeeStatus.OFF_JOB);
  }

  public void appendRoom(Room room) {
    final boolean anyMatch =
        this.rooms.stream().anyMatch(i -> i.getNumber().equals(room.getNumber()));
    if (anyMatch) {
      throw new RuntimeException("房间号重复");
    }
    this.rooms.add(room);
  }

  public Order reserveRoom(RoomDoor roomDoor, Customer customer) {
    final Room targetRoom =
        this.rooms.stream().filter(i -> i.getRoomDoor().equals(roomDoor)).findFirst().orElseThrow();
    if (targetRoom.getStatus() != RoomStatus.FREE) {
      throw new RuntimeException("房间不可用");
    }
    final Order order = OrderFactory.buildByReserveRoom(targetRoom, customer);
    this.orders.add(order);
    // 发送预定消息事件
    new ReserveRoomEventHandler(this)
        .handle(new ReserveRoomEvent(order.getNumber(), ReserveRoomEvent.Status.ORDERED));
    return order;
  }

  // private List<入住信息> checkIns;
  // private List<退房信息> checkOuts;

}
