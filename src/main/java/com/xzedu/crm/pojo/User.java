package com.xzedu.crm.pojo;

public class User {
    private String userId;

    private String uLoginAct;

    private String uLoginPwd;

    private String divName;

    private String uEmail;

    private String uExpireTime;

    private String uLockState;

    private String uDeptno;

    private String uAllowIps;

    private String uCreatetime;

    private String uCreateBy;

    private String uEditTime;

    private String uEditBy;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getuLoginAct() {
        return uLoginAct;
    }

    public void setuLoginAct(String uLoginAct) {
        this.uLoginAct = uLoginAct == null ? null : uLoginAct.trim();
    }

    public String getuLoginPwd() {
        return uLoginPwd;
    }

    public void setuLoginPwd(String uLoginPwd) {
        this.uLoginPwd = uLoginPwd == null ? null : uLoginPwd.trim();
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName == null ? null : divName.trim();
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail == null ? null : uEmail.trim();
    }

    public String getuExpireTime() {
        return uExpireTime;
    }

    public void setuExpireTime(String uExpireTime) {
        this.uExpireTime = uExpireTime == null ? null : uExpireTime.trim();
    }

    public String getuLockState() {
        return uLockState;
    }

    public void setuLockState(String uLockState) {
        this.uLockState = uLockState == null ? null : uLockState.trim();
    }

    public String getuDeptno() {
        return uDeptno;
    }

    public void setuDeptno(String uDeptno) {
        this.uDeptno = uDeptno == null ? null : uDeptno.trim();
    }

    public String getuAllowIps() {
        return uAllowIps;
    }

    public void setuAllowIps(String uAllowIps) {
        this.uAllowIps = uAllowIps == null ? null : uAllowIps.trim();
    }

    public String getuCreatetime() {
        return uCreatetime;
    }

    public void setuCreatetime(String uCreatetime) {
        this.uCreatetime = uCreatetime == null ? null : uCreatetime.trim();
    }

    public String getuCreateBy() {
        return uCreateBy;
    }

    public void setuCreateBy(String uCreateBy) {
        this.uCreateBy = uCreateBy == null ? null : uCreateBy.trim();
    }

    public String getuEditTime() {
        return uEditTime;
    }

    public void setuEditTime(String uEditTime) {
        this.uEditTime = uEditTime == null ? null : uEditTime.trim();
    }

    public String getuEditBy() {
        return uEditBy;
    }

    public void setuEditBy(String uEditBy) {
        this.uEditBy = uEditBy == null ? null : uEditBy.trim();
    }
}