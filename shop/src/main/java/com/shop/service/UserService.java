package com.shop.service;

import com.shop.pojo.User;

import java.util.List;

public interface UserService {
   List<User> isLogin(String username, String userPassword);
   void regesiterUser(User user);
}
