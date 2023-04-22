package org.example.domain.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
  private String id;
  private String name;
  private String phone;
  private EmployeeStatus status; // 状态 [在职、离职]
  private RoleType role; // 角色 [服务员、经理、清洁员]

  public Employee(String id, String name, String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
  }
}
