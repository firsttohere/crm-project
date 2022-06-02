package com.xzedu.crm.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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

	@Override
	public List<User> queryAllUser() {
		return userMapper.selectAllUser();
	}

	@Override
	public Map<String, String> getAllAllowUser() {
		List<User> list = userMapper.selectAllNameAndId();
		HashedMap<String,String> map = new HashedMap<String, String>();
		list.forEach((u) -> map.put(u.getDivName(), u.getUserId()));
		return map;
	}

}
