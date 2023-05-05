package org.example.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.example.client.user.Customer;
import org.springframework.util.CollectionUtils;

@Converter
public class CustomerListConverter implements AttributeConverter<List<Customer>, String> {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<Customer> users) {
    if (CollectionUtils.isEmpty(users)) {
      return "[]";
    }
    try {
      return objectMapper.writeValueAsString(users);
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public List<Customer> convertToEntityAttribute(String dbData) {
    List<Customer> users = new ArrayList<>();
    try {
      Customer[] usersArray = objectMapper.readValue(dbData, Customer[].class);
      users.addAll(Arrays.asList(usersArray));
    } catch (IOException e) {
      // handle exception
    }
    return users;
  }
}
