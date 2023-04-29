package org.example.domain.order;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.example.domain.hotel.Room;
import org.example.domain.user.Customer;

public class OrderFactory {

  public static Order buildByReserveRoom(Room targetRoom, Customer customer, Date reserveDate) {
    final Order order = new Order();
    // 生成uuid
    final String oid = UUID.randomUUID().toString();
    final String oNumber = UUID.randomUUID().toString();
    // order.setId(oid);
    order.setNumber(oNumber);
    settingReserveInfo(customer, order, reserveDate);
    settingPayInfo(targetRoom, order);
    // order.setRoom(targetRoom);
    order.setStatus(OrderStatus.PENDING);
    //    order.setCustomers(new ArrayList<>());
    return order;
  }

  private static void settingPayInfo(Room targetRoom, Order order) {
    final Pay depositPay = PayFactory.buildRefundPay(targetRoom, PayType.DEPOSIT, PayStatus.UNPAID);
    final Pay finalPaymentPay =
        PayFactory.buildRefundPay(targetRoom, PayType.FINAL_PAYMENT, PayStatus.UNPAID);
    final Pay depositCharge =
        PayFactory.buildRefundPay(targetRoom, PayType.DEPOSIT_CHARGE, PayStatus.UNPAID);
    final List<Pay> payList = Arrays.asList(depositPay, finalPaymentPay, depositCharge);
    // order.setPays(new ArrayList<>(payList));
  }

  private static void settingReserveInfo(Customer customer, Order order, Date reserveDate) {
    final ReserveInfo reserve = new ReserveInfo();
    reserve.setCustomer(customer);
    reserve.setReserveDate(reserveDate);
    // order.setReserve(reserve);
  }
}
