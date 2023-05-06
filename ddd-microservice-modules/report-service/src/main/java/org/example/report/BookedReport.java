package org.example.report;

import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.domain.order.BookedStatus;
import org.example.domain.order.Booking;
import org.example.domain.report.BaseReport;
import org.example.domain.report.ReportName;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class BookedReport extends BaseReport {
  private Integer totalCount;
  private Integer successCount;
  private Integer cancelCount;

  public BookedReport() {
    super.setName(ReportName.BOOKED);
  }

  public void reduce(Booking booking) {
    totalCount++;
    if (booking.getStatus() == BookedStatus.BOOKED) {
      successCount++;
    } else if (booking.getStatus() == BookedStatus.CANCELLED) {
      cancelCount++;
    }
  }
}
