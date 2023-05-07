package org.example.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Optional;
import org.example.base.DomainEventPublisher;
import org.example.client.user.Customer;
import org.example.payment.PaymentRemoteService;
import org.example.room.RoomId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock OrderRepository orderRepository;
  @Mock PaymentRemoteService paymentService;
  @InjectMocks OrderService orderService;
  @Mock DomainEventPublisher domainEventPublisher;

  @Test
  void checkIn() {
    final Order order = new Order();
    order.setId(1L);
    order.setNumber("number");
    final RoomId roomId = new RoomId();
    roomId.setId(1L);
    roomId.setNumber("roomNumber");
    order.setRoomId(roomId);
    order.setStatus(OrderStatus.RESERVED);
    order.setCustomers(Lists.newArrayList());
    order.setPhoneOnCheckedIn(null);
    order.setCheckInTime(null);
    order.setCheckOutTime(null);
    when(orderRepository.findById(any())).thenReturn(Optional.of(order));
    when(paymentService.hasUnpaidPayment(any())).thenReturn(false);
    final CheckInReq req = new CheckInReq();
    req.setOrderId(new OrderId(1L, "number"));
    final ArrayList<Customer> customer = Lists.newArrayList();
    final Customer customer1 = new Customer();
    customer1.setName("张三");
    customer1.setIdCard("88888888");
    customer1.setPhone("10086");
    customer.add(customer1);
    req.setCustomer(customer);
    req.setPhone("10086");
    final OrderResp resp = orderService.checkIn(req);
    assertEquals(OrderStatus.CHECKED_IN, resp.getStatus());
    verify(domainEventPublisher).publish(any(OrderCheckedInEvent.class));
  }

  @Test
  void checkOut() {
    final Order order = new Order();
    order.setId(1L);
    order.setNumber("number");
    final RoomId roomId = new RoomId();
    roomId.setId(1L);
    roomId.setNumber("roomNumber");

    order.setRoomId(roomId);
    order.setStatus(OrderStatus.RESERVED);
    order.setCustomers(Lists.newArrayList());
    order.setPhoneOnCheckedIn(null);
    order.setCheckInTime(null);
    order.setCheckOutTime(null);

    when(orderRepository.findById(any())).thenReturn(Optional.of(order));
    final CheckOutReq req = new CheckOutReq();
    req.setOrderId(new OrderId(1L, "number"));
    final OrderResp resp = orderService.checkOut(req);
    assertEquals(OrderStatus.CHECKED_OUT, resp.getStatus());
    verify(domainEventPublisher).publish(any(OrderCheckedOutEvent.class));
  }
}
