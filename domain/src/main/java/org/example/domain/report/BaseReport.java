package org.example.domain.report;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public abstract class BaseReport {
  @Id
  private Long id;
  private ReportType type;
  private ReportName name;
  private Date startDate;
  private Date endDate;
}
