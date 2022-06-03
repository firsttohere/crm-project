package com.xzedu.crm.dao;

import java.util.List;

import com.xzedu.crm.pojo.ActivityRemark;

public interface ActivityRemarkMapper {
    int deleteByPrimaryKey(String activityRemarkId);

    int insert(ActivityRemark record);

    int insertSelective(ActivityRemark record);

    ActivityRemark selectByPrimaryKey(String activityRemarkId);

    int updateByPrimaryKeySelective(ActivityRemark record);

    int updateByPrimaryKey(ActivityRemark record);
    
    //查询所有activityId为xxx的activityRemark
    List<ActivityRemark> getAllByActivityId(String activityId);
    
    //根据activityId批量删除
    int deleteRelativeRemark(String[] activityIds);
    
    //根据id查找一个remark对象，要求外键都实名
    ActivityRemark getOneById(String activityRemarkId);
}