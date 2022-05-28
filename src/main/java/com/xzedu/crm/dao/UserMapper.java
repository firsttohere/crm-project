package com.xzedu.crm.dao;

import java.util.Map;

import com.xzedu.crm.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectUserByLoginActAndPwd(Map<String, Object> map);
}