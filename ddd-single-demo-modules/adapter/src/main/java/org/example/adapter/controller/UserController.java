package org.example.adapter.controller;

import java.util.List;
import javax.annotation.Resource;
import org.example.adapter.dao.UserDao;
import org.example.domain.user.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Resource private UserDao userDao;

  @GetMapping("")
  public List<User> list() {
    return userDao.findAll();
  }

  @PostMapping("")
  public User add(@RequestBody User user) {
    return userDao.save(user);
  }

  @GetMapping("/{id}")
  public User get(@PathVariable Long id) {
    return userDao.findById(id).orElse(null);
  }

  @PutMapping("/{id}")
  public User update(@PathVariable Long id, @RequestBody User user) {
    user.setId(id);
    return userDao.save(user);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    userDao.deleteById(id);
  }
}
