package com.train.shop.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.train.shop.pojo.User;
import com.train.shop.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	
	
// 跳转注册页面
	
	
	@RequestMapping(value="/toregesiter")
	public String toregesiter()
	{
     	return "regesiter";	
	}
	
	
/*	@RequestMapping(value="/{page}")
  public String page(@PathVariable String page)
  {
	 return page; 
  }*/
	
	
	@RequestMapping(value="/login")
	public String login()
	{
		return "login";
	}
	
	
	@RequestMapping(value="/doLogin")
	// 登录方法
	public String checkLogin(String username,
			String userPassword,
			String valideCode,
			HttpSession session,
			Model model
			)
	{
		
		// 取出系统随机给出的验证码
		String vCode =  (String) session.getAttribute("vrifyCode");
		
	   // 1.校验验证码
		if(!vCode.equals(valideCode))
		{
			 // 验证码不正确
			model.addAttribute("errorMsg", "傻逼，验证码不正确");
			return "login";
		}
		
		//2.校验用户名和密码是否正确
		
		
		
		List<User> list = this.userService.isLogin(username, userPassword);
		// 有值
		if(list.size()>0&&list!=null)
		{
			
			//从list集合中拿到User 对象
			User user = list.get(0);
			// 存session域
			session.setAttribute("user",user);
		   return "redirect:/queryAllAuctions";       	 
		}
		else
		{
			model.addAttribute("errorMsg", "账号或密码错误");
			return "login";	
		}
		
		
		
		
		/*User user = this.userService.isLogin(username, userPassword);
		if(user!=null)
		{
			return "index";
		}
		else
		{
			model.addAttribute("errorMsg", "账号或密码错误");
			return "login";
		}*/
		
		
		
		
	}
}
