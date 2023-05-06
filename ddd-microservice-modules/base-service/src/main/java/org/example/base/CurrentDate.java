package org.example.base;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class CurrentDate {
  public Date get() {
    return new Date();
  }
}
