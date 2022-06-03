package com.xzedu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzedu.crm.dao.ActivityRemarkMapper;
import com.xzedu.crm.pojo.ActivityRemark;
import com.xzedu.crm.service.ActivityRemarkService;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService{

	@Autowired
	private ActivityRemarkMapper activityRemarkMapper;
	
	@Override
	public List<ActivityRemark> getAllRemarkByActivityId(String activityId) {
		return activityRemarkMapper.getAllByActivityId(activityId);
	}

	@Override
	public int insertOne(ActivityRemark remark) {
		return activityRemarkMapper.insert(remark);
	}

	@Override
	public int deleteById(String id) {
		return activityRemarkMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ActivityRemark updateSelective(ActivityRemark activityRemark) {
		int effectRows = activityRemarkMapper.updateByPrimaryKeySelective(activityRemark);
		//更新成功，返回新的activityRemark，否则返回null
		if(effectRows == 1) {
			return activityRemarkMapper.getOneById(activityRemark.getActivityRemarkId());
		}
		return null;
	}

}
