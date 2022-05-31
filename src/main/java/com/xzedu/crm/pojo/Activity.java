package com.xzedu.crm.pojo;

public class Activity {
    private String activityId;

    private String activityOwner;

    private String activityName;

    private String activityStartDate;

    private String activityEndDate;

    private String activityCost;

    private String activityDescription;

    private String activityCreateTime;

    private String activityCreateBy;

    private String activityEditTime;

    private String activityEditBy;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getActivityOwner() {
        return activityOwner;
    }

    public void setActivityOwner(String activityOwner) {
        this.activityOwner = activityOwner == null ? null : activityOwner.trim();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(String activityStartDate) {
        this.activityStartDate = activityStartDate == null ? null : activityStartDate.trim();
    }

    public String getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(String activityEndDate) {
        this.activityEndDate = activityEndDate == null ? null : activityEndDate.trim();
    }

    public String getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(String activityCost) {
        this.activityCost = activityCost == null ? null : activityCost.trim();
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription == null ? null : activityDescription.trim();
    }

    public String getActivityCreateTime() {
        return activityCreateTime;
    }

    public void setActivityCreateTime(String activityCreateTime) {
        this.activityCreateTime = activityCreateTime == null ? null : activityCreateTime.trim();
    }

    public String getActivityCreateBy() {
        return activityCreateBy;
    }

    public void setActivityCreateBy(String activityCreateBy) {
        this.activityCreateBy = activityCreateBy == null ? null : activityCreateBy.trim();
    }

    public String getActivityEditTime() {
        return activityEditTime;
    }

    public void setActivityEditTime(String activityEditTime) {
        this.activityEditTime = activityEditTime == null ? null : activityEditTime.trim();
    }

    public String getActivityEditBy() {
        return activityEditBy;
    }

    public void setActivityEditBy(String activityEditBy) {
        this.activityEditBy = activityEditBy == null ? null : activityEditBy.trim();
    }
}