package com.train.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.train.shop.mapper.UserMapper;
import com.train.shop.pojo.User;
import com.train.shop.service.UserService;

@Controller
public class RegesiterController {
	
	@Autowired
	private UserService userService;
	@RequestMapping(value="/regesiter")
  public String regesiter(User user)
  {
		
		
		// 设置用户的权限  0 普通用户 1管理员
		user.setUserisadmin(0);
		
	  this.userService.regesiterUser(user);
	  
	  return "redirect:/login";
  }
}
