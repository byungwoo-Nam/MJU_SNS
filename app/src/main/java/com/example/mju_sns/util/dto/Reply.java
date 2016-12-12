package com.example.mju_sns.util.dto;

import java.io.Serializable;

public class Reply implements Serializable {
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getWritings_seq() {
        return writings_seq;
    }

    public void setWritings_seq(int user_seq) {
        this.writings_seq = writings_seq;
    }

    public int getUser_seq() {
        return user_seq;
    }

    public void setUser_seq(int user_seq) {
        this.user_seq = user_seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }

    private int seq;
    private int writings_seq;
    private int user_seq;
    private String content;
    private boolean mine;
}
