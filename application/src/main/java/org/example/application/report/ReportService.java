package org.example.application.report;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Range;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.example.domain.order.Booking;
import org.example.domain.order.BookingRepository;
import org.example.domain.order.Order;
import org.example.domain.order.OrderRepository;
import org.example.domain.payment.Payment;
import org.example.domain.payment.PaymentRepository;
import org.example.domain.report.BookedReport;
import org.example.domain.report.BookedReportRepository;
import org.example.domain.report.CheckedInReport;
import org.example.domain.report.CheckedInReportRepository;
import org.example.domain.report.PaymentReport;
import org.example.domain.report.PaymentReportRepository;
import org.example.domain.report.ReportName;
import org.example.domain.report.ReportType;
import org.example.domain.room.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
  private final BookingRepository bookingRepository;
  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;

  private final BookedReportRepository bookedReportRepository;
  private final CheckedInReportRepository checkedInReportRepository;
  private final PaymentReportRepository paymentReportRepository;
  private final RoomRepository roomRepository;

  public ReportService(
      BookingRepository bookingRepository,
      PaymentRepository paymentRepository,
      OrderRepository orderRepository,
      BookedReportRepository bookedReportRepository,
      CheckedInReportRepository checkedInReportRepository,
      PaymentReportRepository paymentReportRepository,
      RoomRepository roomRepository) {
    this.bookingRepository = bookingRepository;
    this.paymentRepository = paymentRepository;
    this.orderRepository = orderRepository;
    this.bookedReportRepository = bookedReportRepository;
    this.checkedInReportRepository = checkedInReportRepository;
    this.paymentReportRepository = paymentReportRepository;
    this.roomRepository = roomRepository;
  }

  /**
   * todo 建立定时器周期触发统计过程
   * @param reportName ~
   * @param reportType ~
   * @param range ~
   */
  public void generateReport(ReportName reportName, ReportType reportType, Range<Date> range) {
    if (reportName == ReportName.BOOKED) {
      generateBookedReport(reportName, reportType, range);
    }
    if (reportName == ReportName.CHECKED_IN) {
      generateCheckInReport(reportName, reportType, range);
    }
    if (reportName == ReportName.PAYMENT) {
      generatePaymentReport(reportName, reportType, range);
    }
  }

  private void generatePaymentReport(
      ReportName reportName, ReportType reportType, Range<Date> range) {
    final PaymentReport paymentReport = new PaymentReport();
    paymentReport.setName(reportName);
    paymentReport.setType(reportType);
    paymentReport.setStartDate(range.lowerEndpoint());
    paymentReport.setEndDate(range.upperEndpoint());
    recursion(
        (startId) ->
            paymentRepository.findFirst1000ByCreatedAtBetweenAndIdGreaterThanEqualOrderByIdAsc(
                range.lowerEndpoint(), range.upperEndpoint(), startId ),
        (list) -> list.forEach(paymentReport::reduce),
        Payment::getId);
    paymentReportRepository.save(paymentReport);
  }

  private void generateCheckInReport(
      ReportName reportName, ReportType reportType, Range<Date> range) {
    final CheckedInReport checkedInReport = new CheckedInReport();
    checkedInReport.setName(reportName);
    checkedInReport.setType(reportType);
    checkedInReport.setStartDate(range.lowerEndpoint());
    checkedInReport.setEndDate(range.upperEndpoint());
    final int between =
        Math.toIntExact(
            DateUtil.between(
                checkedInReport.getStartDate(), checkedInReport.getEndDate(), DateUnit.DAY));
    final int roomCount = Math.toIntExact(roomRepository.count());
    recursion(
        (startId) ->
            orderRepository.findFirst1000ByCheckInTimeBetweenAndIdGreaterThanEqualOrderByIdAsc(
                range.lowerEndpoint(), range.upperEndpoint(), startId),
        (list) -> list.forEach(o -> checkedInReport.reduce(o, between, roomCount)),
        Order::getId);
    checkedInReportRepository.save(checkedInReport);
  }

  private void generateBookedReport(
      ReportName reportName, ReportType reportType, Range<Date> range) {
    final BookedReport bookedReport = new BookedReport();
    bookedReport.setTotalCount(0);
    bookedReport.setName(reportName);
    bookedReport.setType(reportType);
    bookedReport.setStartDate(range.lowerEndpoint());
    bookedReport.setEndDate(range.upperEndpoint());
    recursion(
        (startId) ->
            bookingRepository.findFirst1000ByCheckInDateBetweenAndIdGreaterThanEqualOrderByIdAsc(
                range.lowerEndpoint(), range.upperEndpoint(), startId),
        (list) -> list.forEach(bookedReport::reduce),
        Booking::getId);
    bookedReportRepository.save(bookedReport);
  }

  private <T> void recursion(
      Function<Long, List<T>> function, Consumer<List<T>> consumer, Function<T, Long> getId) {
    Long startId = 1L;
    while (true) {
      final List<T> list = function.apply(startId);
      if (list.isEmpty()) {
        return;
      }
      consumer.accept(list);
      startId = list.stream().map(getId).max(Long::compareTo).orElse(Long.MAX_VALUE);
    }
  }
}
