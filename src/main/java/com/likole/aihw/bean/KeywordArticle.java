package com.likole.aihw.bean;

import org.nutz.dao.entity.annotation.*;

@Table
@PK({"keyword","wos"})
public class KeywordArticle {

    @Column
    private String keyword;

    @Column
    private String wos;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String title;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getWos() {
        return wos;
    }

    public void setWos(String wos) {
        this.wos = wos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
