package com.zhangsong.com.signup_system.adapter.FragmentPagetAdapter;

import java.io.Serializable;

public class NoticeEntity implements Serializable {
    public String NoticeTitle;//公告标题
    public String NoticeTime;//公告时间
    public String NoticeContent;//公告内容

    public String getNoticeSource() {
        return NoticeSource;
    }

    public void setNoticeSource(String noticeSource) {
        NoticeSource = noticeSource;
    }

    public String NoticeSource;//公告来源

    public NoticeEntity() {
    }

    public String getNoticeContent() {
        return NoticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        NoticeContent = noticeContent;
    }

    public NoticeEntity(String NoticeContent, String noticeTitle, String noticeTime) {
        this.NoticeTitle = noticeTitle;
        this.NoticeTime = noticeTime;
        this.NoticeContent=NoticeContent;
    }

    public String getNoticeTitle() {
        return NoticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.NoticeTitle = noticeTitle;
    }

    public String getNoticeTime() {
        return NoticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.NoticeTime = noticeTime;
    }
}
