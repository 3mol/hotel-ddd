package org.example.domain.order;

import java.util.Date;
import java.util.List;
import lombok.Data;
import org.example.domain.hotel.Room;
import org.example.domain.user.Customer;

@Data
public class Order {
  private String id;
  private String number; // 订单号;
  private Room room; // 房间;
  private OrderStatus status; // 状态 [未入住、已入住、已退房];
  private List<Customer> customers; // 旅客信息;
  private ReserveInfo reserve; // 预定信息;
  private List<Pay> pays; // 支付信息;
  private Date checkInTime; // 入住时间;
  private Date checkOutTime; // 退房时间;
}
