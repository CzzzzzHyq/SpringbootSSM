package com.train.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.train.shop.mapper.UserMapper;
import com.train.shop.pojo.User;
import com.train.shop.pojo.UserExample;
import com.train.shop.pojo.UserExample.Criteria;


@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> isLogin(String username, String userPassword) {
		UserExample example = new UserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		createCriteria.andUserpasswordEqualTo(userPassword);
		List<User> list = this.userMapper.selectByExample(example);
		return list;
	}

	@Override
	public void regesiterUser(User user) {
		
		this.userMapper.insert(user);
		
	}
	
	/*public User isLogin(String username, String userPassword) {
		
		UserExample example = new UserExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		createCriteria.andUserpasswordEqualTo(userPassword);
		List<User> list = this.userMapper.selectByExample(example);
		return list.get(0);
	}*/

	
	
	
}
