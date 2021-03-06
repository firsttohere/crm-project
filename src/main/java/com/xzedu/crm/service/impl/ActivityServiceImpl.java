package com.xzedu.crm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzedu.crm.dao.ActivityMapper;
import com.xzedu.crm.dao.ActivityRemarkMapper;
import com.xzedu.crm.pojo.Activity;
import com.xzedu.crm.service.ActivityService;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityMapper activityMapper;

	@Autowired
	private ActivityRemarkMapper activityRemarkMapper;
	
	@Override
	public int createActivity(Activity activity) {
		//调用dao层的方法
		return activityMapper.insertSelective(activity);
	}

	@Override
	public int queryCount() {
		return activityMapper.queryCount();
	}

	@Override
	public List<Activity> queryByPage(Map<String, Object> map) {
		Integer pageNo = Integer.parseInt((String) map.get("pageNo"));
		Integer pageSize = Integer.parseInt((String) map.get("pageSize"));
		//计算出pageNo对应的left
		int count = queryCount();
		if (pageNo < 1 || pageSize < 1 || count <= (pageNo - 1) * pageSize) {
			throw new RuntimeException("参数不对或者没有那么多记录");
		}
		int newSize = pageSize;
		if (pageNo * pageSize > count) {
			newSize = count - (pageNo - 1) * pageSize;
		}
		//这里的activity中的owner是user表主键的外键
		map.put("pageNo", (pageNo - 1) * pageSize);
		map.put("pageSize", newSize);
		
		return activityMapper.queryByPageNoAndPageSize(map);
	}

	@Override
	public int batchDelete(String[] ids) {
		
		//删除哪些activityRemark的acivityId在ids中的备注
		activityRemarkMapper.deleteRelativeRemark(ids);
		
		int deleteActivityCount = activityMapper.batchDelete(ids);
		
		return deleteActivityCount;
	}

	@Override
	public Activity queryById(String id) {
		return activityMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updatePart(Activity activity) {
		return activityMapper.updateByPrimaryKeySelective(activity);
	}

	@Override
	public List<Activity> queryAll() {
		return activityMapper.queryAll();
	}

	@Override
	public List<Activity> batchQuery(String[] ids) {
		return activityMapper.batchQuery(ids);
	}

	@Override
	public int batchInsert(List<Activity> list) {
		return activityMapper.batchInsert(list);
	}

	@Override
	public Activity selectById(String activityId) {
		return activityMapper.selectById(activityId);
	}

}
