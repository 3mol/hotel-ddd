package org.example.domain.order;

import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RoomId {
  private Long id;
  private String number;
}
