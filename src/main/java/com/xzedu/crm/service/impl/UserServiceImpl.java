package com.xzedu.crm.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzedu.crm.dao.UserMapper;
import com.xzedu.crm.pojo.User;
import com.xzedu.crm.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User queryUserByLoginActAndPwd(Map<String, Object> map) {
		return userMapper.selectUserByLoginActAndPwd(map);
	}

}
