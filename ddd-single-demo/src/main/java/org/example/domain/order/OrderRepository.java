package org.example.domain.order;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Order findByNumber(String orderNumber);

  List<Order> findAllByCheckInTimeBetweenAndIdGreaterThanEqualOrderByIdAsc(
      Date startCheckInTime, Date endCheckInTime, Long startId, int size);
}
