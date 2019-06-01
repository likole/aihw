package com.likole.aihw.bean;

import org.nutz.dao.entity.annotation.*;

@Table
@PK({"authorFullName","wos"})
public class AuthorArticle {

    @Column
    private String authorFullName;

    @Column
    private String wos;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String title;

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
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
