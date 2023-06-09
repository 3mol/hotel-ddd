package org.example.domain.order;

import org.example.application.order.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  Booking findByOrderId(OrderId orderId);
}
