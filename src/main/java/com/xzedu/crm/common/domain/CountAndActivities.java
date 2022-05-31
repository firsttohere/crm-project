package com.xzedu.crm.common.domain;

import java.io.Serializable;
import java.util.List;

import com.xzedu.crm.pojo.Activity;

public class CountAndActivities implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer count;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	List<Activity> activities;
	
}
