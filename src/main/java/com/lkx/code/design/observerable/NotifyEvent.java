package com.lkx.code.design.observerable;

// 通知事件类
public class NotifyEvent {

    private String mobileNo;

    private String emailNo;

    private String imNo;

    public NotifyEvent(String mobileNo, String emailNo, String imNo) {
        this.mobileNo = mobileNo;
        this.emailNo = emailNo;
        this.imNo = imNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailNo() {
        return emailNo;
    }

    public void setEmailNo(String emailNo) {
        this.emailNo = emailNo;
    }

    public String getImNo() {
        return imNo;
    }

    public void setImNo(String imNo) {
        this.imNo = imNo;
    }
}