package org.example.order;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.example.client.user.Customer;
import org.example.client.user.UserId;
import org.example.room.RoomId;

@Data
@Entity
@Table(name = "booking")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "order_id")),
    @AttributeOverride(name = "number", column = @Column(name = "order_number"))
  })
  @Embedded
  private OrderId orderId;

  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "room_id")),
    @AttributeOverride(name = "number", column = @Column(name = "room_number"))
  })
  @Embedded
  private RoomId roomId;

  @AttributeOverrides({
     @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
     @AttributeOverride(name = "idCard", column = @Column(name = "customer_id_card")),
     @AttributeOverride(name = "phone", column = @Column(name = "customer_phone")),
  })
  @Embedded
  private Customer customer;

  private String phone;
  private Date checkInDate;

  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "ordered_user_id")),
    @AttributeOverride(name = "name", column = @Column(name = "ordered_user_name")),
  })
  @Embedded
  private UserId orderedUserId;

  private BookedStatus status;

  public void setOrderedUserId(UserId orderedUserId) {
    this.orderedUserId = orderedUserId;
  }

  public UserId getOrderedUserId() {
    return orderedUserId;
  }
}
