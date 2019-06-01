package com.train.shop.service;

import java.util.List;

import com.train.shop.pojo.User;

public interface UserService {
 // User isLogin(String username,String userPassword);

  
  List<User> isLogin(String username, String userPassword);

 void regesiterUser(User user);
}
