package com.likole.aihw.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table
public class Author {

    @Name
    private String authorFullName;

    @Column
    private String authorShortName;

    @Column
    @Default("0")
    private int articleNumbers;


    public int getArticleNumbers() {
        return articleNumbers;
    }

    public void setArticleNumbers(int articleNumbers) {
        this.articleNumbers = articleNumbers;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getAuthorShortName() {
        return authorShortName;
    }

    public void setAuthorShortName(String authorShortName) {
        this.authorShortName = authorShortName;
    }
}
