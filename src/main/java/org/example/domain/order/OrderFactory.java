package org.example.domain.order;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import org.example.domain.hotel.Room;
import org.example.domain.user.Customer;

public class OrderFactory {

  public static Order buildByReserveRoom(Room targetRoom, Customer customer) {
    final Order order = new Order();
    // 生成uuid
    final String oid = UUID.randomUUID().toString();
    final String oNumber = UUID.randomUUID().toString();
    order.setId(oid);
    order.setNumber(oNumber);
    settingReserveInfo(customer, order);
    settingPayInfo(targetRoom, order);
    order.setRoom(targetRoom);
    order.setStatus(OrderStatus.PENDING);
    return order;
  }

  private static void settingPayInfo(Room targetRoom, Order order) {
    final Pay depositPay = PayFactory.buildPay(targetRoom, PayType.DEPOSIT, PayStatus.UNPAID);
    final Pay finalPaymentPay =
        PayFactory.buildPay(targetRoom, PayType.FINAL_PAYMENT, PayStatus.UNPAID);
    final Pay depositCharge =
        PayFactory.buildPay(targetRoom, PayType.DEPOSIT_CHARGE, PayStatus.UNPAID);
    order.setPays(Arrays.asList(depositPay, finalPaymentPay, depositCharge));
  }

  private static void settingReserveInfo(Customer customer, Order order) {
    final ReserveInfo reserve = new ReserveInfo();
    reserve.setCustomer(customer);
    reserve.setReserveTime(new Date());
    order.setReserve(reserve);
  }
}
