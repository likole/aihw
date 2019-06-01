package com.likole.aihw.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableMeta;

@Table
@PK({"fromWOS","toWOS"})
public class ArticleReference {
    @Column
    private String fromWOS;

    @Column
    private String fromTitle;

    @Column
    private String toWOS;

    @Column
    private String toTitle;

    public String getFromWOS() {
        return fromWOS;
    }

    public void setFromWOS(String fromWOS) {
        this.fromWOS = fromWOS;
    }

    public String getFromTitle() {
        return fromTitle;
    }

    public void setFromTitle(String fromTitle) {
        this.fromTitle = fromTitle;
    }

    public String getToWOS() {
        return toWOS;
    }

    public void setToWOS(String toWOS) {
        this.toWOS = toWOS;
    }

    public String getToTitle() {
        return toTitle;
    }

    public void setToTitle(String toTitle) {
        this.toTitle = toTitle;
    }
}
