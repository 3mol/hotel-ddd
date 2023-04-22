package org.example.domain.hotel;

import cn.hutool.db.sql.Order;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.domain.order.Pay;

@Data
@AllArgsConstructor
public class Hotel {
  private String id;
  private String name;
  private String address;
  private String phone;
  private HotelStatus status;
  private String description;
  private List<Room> rooms;
  private List<Employee> employees;
  private List<Order> reserves;
  private List<Pay> pays;

  public Hotel(
      String id,
      String name,
      String address,
      String phone,
      HotelStatus status,
      String description) {
    this(
        id,
        name,
        address,
        phone,
        status,
        description,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public void employeeOnboarding(Employee employee, RoleType roleType) {
    employee.setStatus(EmployeeStatus.ON_JOB);
    employee.setRole(roleType);
    this.employees.add(employee);
  }

  public void employeeTurnover(Employee employee) {
    this.employees.stream()
        .filter(i -> i.equals(employee))
        .findFirst()
        .orElseThrow()
        .setStatus(EmployeeStatus.OFF_JOB);
  }

  public void appendRoom(Room room) {
    final boolean anyMatch =
        this.rooms.stream().anyMatch(i -> i.getNumber().equals(room.getNumber()));
    if (anyMatch) {
      throw new RuntimeException("房间号重复");
    }
    this.rooms.add(room);
  }
  // private List<入住信息> checkIns;
  // private List<退房信息> checkOuts;

}
