package org.example.order;

public interface PaymentRemoteService {
  Boolean hasUnpaidPayment(String number);
}
