package org.example.domain.payment;

import java.util.Date;
import java.util.List;
import org.example.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  List<Payment> findAllBySerialNumber(String number);

  List<Payment> findAllByThirdPartySerialNumber(String thirdPartySerialNumber);

  List<Payment> findAllByCreatedAtBetweenAndIdGreaterThanEqualOrderByIdAsc(Date startCreatedAt, Date endCreatedAt, Long startId, int size);
}
