package org.example.report;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.domain.order.PayStatus;
import org.example.domain.order.PayType;
import org.example.domain.payment.Payment;

@Data
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentReport extends BaseReport {
  /** 预定时产生的总金额 = 预定成功的金额 + 尾款 + 已退款金额 + 不需要退款的金额 */
  private Double totalAmount;
  /** 预定成功的金额, 酒店应收的订金 */
  private Double bookedAmount;
  /** 尾款 */
  private Double finalAmount;
  /** 已退款金额 */
  private Double refundedAmount;
  /** 不需要退款的金额 */
  private Double noRefundedAmount;

  public PaymentReport() {
    super.setName(ReportName.PAYMENT);
  }

  public void reduce(Payment payment) {
    totalAmount += payment.getAmount();
    if (payment.getType() == PayType.DEPOSIT) {
      bookedAmount += payment.getAmount();
    }
    if (payment.getType() == PayType.FINAL_PAYMENT) {
      finalAmount += payment.getAmount();
    }
    if (payment.getStatus() == PayStatus.REFUNDED) {
      refundedAmount += payment.getAmount();
    }
    if (payment.getStatus() == PayStatus.NO_REFUND) {
      noRefundedAmount += payment.getAmount();
    }
  }
}
