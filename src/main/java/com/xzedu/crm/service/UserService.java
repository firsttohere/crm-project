package com.xzedu.crm.service;

import java.util.List;
import java.util.Map;

import com.xzedu.crm.pojo.User;

public interface UserService {
	User queryUserByLoginActAndPwd(Map<String, Object> map);
	List<User> queryAllUser();
}
