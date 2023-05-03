package org.example.Infra.gateway;

import java.util.Date;

public class WechatRefundReq {
  private RefundStatus refundStatus;
  private String serialNumber;
  private double refundAmount;
  private Date refundTime;

  public void setRefundStatus(RefundStatus refundStatus) {
    this.refundStatus = refundStatus;
  }

  public RefundStatus getRefundStatus() {
    return refundStatus;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setRefundAmount(double refundAmount) {
    this.refundAmount = refundAmount;
  }

  public double getRefundAmount() {
    return refundAmount;
  }

  public void setRefundTime(Date refundTime) {
    this.refundTime = refundTime;
  }

  public Date getRefundTime() {
    return refundTime;
  }

  public enum RefundStatus {
    SUCCESS,
    FAIL
  }
}
