package net.him0.voiceserver;

import java.util.Date;

/**
 * Created by him0 on 2016/02/14.
 */
public class Statement {
    private long id;
    private Date date = null;
    private String content = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return date.toString();
    }

    public void setDateStamp(Date d) {
        date = d;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String c) {
        content = c;
    }
}
