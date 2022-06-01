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
}
