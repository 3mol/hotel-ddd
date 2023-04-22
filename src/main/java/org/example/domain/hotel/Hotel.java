package org.example.domain.hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.application.PayService;
import org.example.domain.order.Order;
import org.example.domain.order.OrderFactory;
import org.example.domain.order.OrderStatus;
import org.example.domain.order.Pay;
import org.example.domain.order.PayMethod;
import org.example.domain.order.PayType;
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

  public void acceptPay(Order order, PayType payType, PayMethod payMethod, double amount) {
    PayService.pay(order, payType, payMethod, amount);
    if (payType == PayType.DEPOSIT) {
      order.setStatus(OrderStatus.RESERVED);
      order.getRoom().setStatus(RoomStatus.RESERVED);
    }
    if (payType == PayType.FINAL_PAYMENT) {
      // ignore
    }
    if (payType == PayType.DEPOSIT_CHARGE) {
      // ignore
    }
  }

  public RoomCard checkIn(Order order, Customer customer) {
    order.setStatus(OrderStatus.CHECKED);
    order.getRoom().setStatus(RoomStatus.CHECKED_IN);
    order.getCustomers().add(customer);
    order.setCheckInTime(new Date());
    new CheckedInEventHandler(this).handle(new CheckedInEvent(order.getNumber()));
    // 设置锁的密钥，并且写入密钥到房卡
    final String key = UUID.randomUUID().toString();
    order.getRoom().getRoomDoor().getRoomLock().setKey(key);
    return new RoomCard(key);
  }
}
