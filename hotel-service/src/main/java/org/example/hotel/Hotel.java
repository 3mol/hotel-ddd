package org.example.hotel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "hotel")
public class Hotel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String address;
  private String phone;
  private HotelStatus status;
  private String description;

  public Hotel() {}

  //  public void employeeOnboarding(Employee employee, RoleType roleType) {
  //    employee.setStatus(EmployeeStatus.ON_JOB);
  //    employee.setRole(roleType);
  //    this.employees.add(employee);
  //  }
  //
  //  public void employeeTurnover(Employee employee) {
  //    this.employees.stream()
  //       .filter(i -> i.equals(employee))
  //       .findFirst()
  //       .orElseThrow()
  //       .setStatus(EmployeeStatus.OFF_JOB);
  //  }
  //
  //  public void appendRoom(Room room) {
  //    final boolean anyMatch =
  //       this.rooms.stream().anyMatch(i -> i.getNumber().equals(room.getNumber()));
  //    if (anyMatch) {
  //      throw new RuntimeException("房间号重复");
  //    }
  //    this.rooms.add(room);
  //  }
  //
  //  public Order reserveRoom(String roomNumber, Customer customer, Date reserveDate) {
  //    final Room targetRoom =
  //       this.rooms.stream().filter(i ->
  // i.getNumber().equals(roomNumber)).findFirst().orElseThrow();
  //    if (targetRoom.getStatus() != RoomStatus.FREE) {
  //      throw new RuntimeException("房间不可用");
  //    }
  //    final Order order = OrderFactory.buildByReserveRoom(targetRoom, customer, reserveDate);
  //    this.orders.add(order);
  //    // 发送预定消息事件
  //    new ReserveRoomEventHandler(this)
  //       .handle(new ReserveRoomEvent(order.getNumber(), ReserveRoomEvent.Status.ORDERED));
  //    return order;
  //  }
  //
  //  // private List<入住信息> checkIns;
  //  // private List<退房信息> checkOuts;
  //
  //  public void acceptPay(Order order, PayType payType, PayMethod payMethod, double amount) {
  //    // PayService.pay(order, payType, payMethod, amount);
  //    if (payType == PayType.DEPOSIT) {
  //      order.setStatus(OrderStatus.RESERVED);
  //      order.getRoom().setStatus(RoomStatus.RESERVED);
  //    }
  //    if (payType == PayType.FINAL_PAYMENT) {
  //      // ignore
  //    }
  //    if (payType == PayType.DEPOSIT_CHARGE) {
  //      // ignore
  //    }
  //  }
  //
  //  public RoomCard checkIn(Order order, Customer customer) {
  //    order.setStatus(OrderStatus.CHECKED);
  //    order.getRoom().setStatus(RoomStatus.CHECKED_IN);
  //    order.getCustomers().add(customer);
  //    order.setCheckInTime(new Date());
  //    new CheckedInEventHandler(this).handle(new CheckedInEvent(order.getNumber()));
  //    // 设置锁的密钥，并且写入密钥到房卡
  //    final String key = UUID.randomUUID().toString();
  //    order.getRoom().getRoomDoor().getRoomLock().setKey(key);
  //    return new RoomCard(key);
  //  }
  //
  //  public void checkOut(RoomCard roomCard) {
  //    final String key = roomCard.getKey();
  //    final Room room =
  //       this.rooms.stream()
  //          .filter(i -> i.getRoomDoor().getRoomLock().getKey().equals(key))
  //          .findFirst()
  //          .orElseThrow();
  //    room.setStatus(RoomStatus.CHECKED_OUT);
  //    room.getRoomDoor().getRoomLock().setKey("");
  //    final Order order =
  //       this.orders.stream().filter(i -> i.getRoom().equals(room)).findFirst().orElseThrow();
  //    order.setStatus(OrderStatus.CHECKED_OUT);
  //    // 退款
  //    // PayService.refund(order, PayType.DEPOSIT_CHARGE, 1);
  //    // 发送退房消息事件
  //    new CheckOutEventHandler(this).handle(new CheckOutEvent(order.getNumber(),
  // room.getNumber()));
  //  }
  //
  //  public void cancelReserve(Order order, Date cancelDate) {
  //    // 预定时间在24小时之内 不退押金
  //    final long betweenHour =
  //       DateUtil.between(cancelDate, order.getReserve().getReserveDate(), DateUnit.HOUR);
  //    if (betweenHour <= 24) {
  //      order.setStatus(OrderStatus.CANCELLED);
  //      order.getRoom().setStatus(RoomStatus.FREE);
  //      return;
  //    }
  //    // 预定时间在24小时之外 退80%押金
  //    // PayService.refund(order, PayType.DEPOSIT, 0.8);
  //    order.setStatus(OrderStatus.CANCELLED);
  //    order.getRoom().setStatus(RoomStatus.FREE);
  //  }
}
