package com.example.mju_sns.util.dto;

public class Users {
    private int seq;
    private String id;
    private String gcm_id;
    private String nickname;
    private boolean push_on_off;
    private String current_location;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isPush_on_off() {
        return push_on_off;
    }

    public void setPush_on_off(boolean push_on_off) {
        this.push_on_off = push_on_off;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }
}
