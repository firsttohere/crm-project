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
	//批量删除
	int batchDelete(String[] ids);
	//根据id查询一个activity
	Activity queryById(String id);
	//按照选择的字段更新
	int updatePart(Activity activity);
	//查询所有活动
	List<Activity> queryAll();
	//批量查询
	List<Activity> batchQuery(String[] ids);
	//批量添加
	int batchInsert(List<Activity> list);
	
	//根据id查找，其中owner是真名
	Activity selectById(String activityId);
}
