package org.example.application.room;

import lombok.Data;

@Data
public class RoomResp {
  private final Long id;

  public RoomResp(Long id) {

    this.id = id;
  }
}
