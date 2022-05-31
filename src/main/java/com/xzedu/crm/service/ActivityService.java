package com.xzedu.crm.service;

import java.util.List;
import java.util.Map;

import com.xzedu.crm.pojo.Activity;

public interface ActivityService {
	//创建一个活动
	public int createActivity(Activity activity);
	//查询总记录条数
	int queryCount();
	//分页查询
	List<Activity> queryByPage(Map<String, Object> map);
}
