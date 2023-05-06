package org.example.order;

public interface BookingRemoteService {
  Booking findByOrderId(OrderId orderId);
}
