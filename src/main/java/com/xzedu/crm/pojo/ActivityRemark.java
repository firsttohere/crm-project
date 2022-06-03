package com.xzedu.crm.pojo;

public class ActivityRemark {
    private String activityRemarkId;

    private String activityId;

    private String arNoteContent;

    private String arCreateTime;

    private String arCreateBy;

    private String arEditTime;

    private String arEditBy;

    private String arEditFlag;

    public String getActivityRemarkId() {
        return activityRemarkId;
    }

    public void setActivityRemarkId(String activityRemarkId) {
        this.activityRemarkId = activityRemarkId == null ? null : activityRemarkId.trim();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getArNoteContent() {
        return arNoteContent;
    }

    public void setArNoteContent(String arNoteContent) {
        this.arNoteContent = arNoteContent == null ? null : arNoteContent.trim();
    }

    public String getArCreateTime() {
        return arCreateTime;
    }

    public void setArCreateTime(String arCreateTime) {
        this.arCreateTime = arCreateTime == null ? null : arCreateTime.trim();
    }

    public String getArCreateBy() {
        return arCreateBy;
    }

    public void setArCreateBy(String arCreateBy) {
        this.arCreateBy = arCreateBy == null ? null : arCreateBy.trim();
    }

    public String getArEditTime() {
        return arEditTime;
    }

    public void setArEditTime(String arEditTime) {
        this.arEditTime = arEditTime == null ? null : arEditTime.trim();
    }

    public String getArEditBy() {
        return arEditBy;
    }

    public void setArEditBy(String arEditBy) {
        this.arEditBy = arEditBy == null ? null : arEditBy.trim();
    }

    public String getArEditFlag() {
        return arEditFlag;
    }

    public void setArEditFlag(String arEditFlag) {
        this.arEditFlag = arEditFlag == null ? null : arEditFlag.trim();
    }
}