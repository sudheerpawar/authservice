package com.loontao.authservice.dto;

public class WebhookPayload {
    private String event_type;
    private String mobile_no;

    // Constructor
    public WebhookPayload(String eventType, String mobileNo) {
        this.event_type = eventType;
        this.mobile_no = mobileNo;
    }

    // Getters and Setters
    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
