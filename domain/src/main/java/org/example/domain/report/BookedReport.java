package org.example.domain.report;

import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.domain.order.BookedStatus;
import org.example.domain.order.Booking;

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
    if (booking.getStatus()== BookedStatus.BOOKED) {
      successCount++;
    } else if (booking.getStatus()== BookedStatus.CANCELLED){
      cancelCount++;
    }
  }
}
