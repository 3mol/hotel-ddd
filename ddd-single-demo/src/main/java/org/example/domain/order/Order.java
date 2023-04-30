package org.example.domain.order;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.example.domain.user.Customer;

@Data
@Table(name = "hotel_order")
@Entity
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String number; // 订单号;

  @AttributeOverrides({
     @AttributeOverride(name = "id", column = @Column(name = "room_id")),
     @AttributeOverride(name = "number", column = @Column(name = "room_number"))
  })
  @Embedded
  private RoomId roomId; // 房间;

  private OrderStatus status; // 状态 [未入住、已入住、已退房];
  @Convert(converter = CustomerListConverter.class)
  private List<Customer> customers; // 旅客信息;
  private String phoneOnCheckedIn; // 入住时预留的手机号
  private Date checkInTime; // 入住时间;
  private Date checkOutTime; // 退房时间;

  public void checkIn(List<Customer> customers, String phone) {
    this.status = OrderStatus.CHECKED;
    this.checkInTime = new Date();
    this.phoneOnCheckedIn = phone;
    customers.forEach(this::addCustomer);
  }

  public void addCustomer(Customer customer) {
    customers.add(customer);
  }

  public void checkOut() {
    this.status = OrderStatus.CHECKED_OUT;
  }
}
