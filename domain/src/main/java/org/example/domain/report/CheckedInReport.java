package org.example.domain.report;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.domain.order.Order;

@Data
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CheckedInReport extends BaseReport {
  /** 入住人数 */
  private Integer numberOfCustomers;
  /** 入住房间数 */
  private Integer numberOfRooms;
  /*
   * 房间入住率 = 入住房间数 / 天数 / 总房间数量
   */
  private Double ratioOfCheckIn;

  public CheckedInReport() {
    super.setName(ReportName.CHECKED_IN);
    numberOfCustomers = 0;
    numberOfRooms = 0;
    ratioOfCheckIn = 0d;
  }

  public void reduce(Order order, Integer days, Integer numberOfRoom) {
    this.numberOfCustomers += order.getCustomers().size();
    this.numberOfRooms += 1;
    this.ratioOfCheckIn = this.numberOfRooms / (double) days / (double) numberOfRoom;
  }
}
