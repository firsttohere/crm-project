package com.xzedu.crm.dao;

import java.util.List;
import java.util.Map;


import com.xzedu.crm.pojo.Activity;

public interface ActivityMapper {
    int deleteByPrimaryKey(String activityId);

    int insert(Activity record);

    int insertSelective(Activity record);

    //根据主键查找Activity，其中owner是id
    Activity selectByPrimaryKey(String activityId);
    
    //根据主键查找Activity，其中owner是真名
    Activity selectById(String activityId);
    
    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);
    
    //查询所有记录条数
    int queryCount();
    
    //分页查询
    List<Activity> queryByPageNoAndPageSize(Map<String, Object> map);
    
    //批量删除
    int batchDelete(String[] ids) ;
    
    //查询所有记录
    List<Activity> queryAll();
    
    //批量查询
    List<Activity> batchQuery(String[] ids);
    
    //批量添加
    int batchInsert(List<Activity> list);
}