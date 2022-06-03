package com.xzedu.crm.service;

import java.util.List;

import com.xzedu.crm.pojo.ActivityRemark;

public interface ActivityRemarkService {
	//根据activityId查询所有的相关remark
	List<ActivityRemark> getAllRemarkByActivityId(String activityId);
	//插入一条备注
	int insertOne(ActivityRemark remark);
	//g根据id值删除
	int deleteById(String id);
	//根据id更新部分，返回最新的remark
	ActivityRemark updateSelective(ActivityRemark activityRemark);
}
