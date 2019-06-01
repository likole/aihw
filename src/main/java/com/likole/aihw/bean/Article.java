package com.likole.aihw.bean;

import org.nutz.dao.entity.annotation.*;

/**
 * @author likole
 */
@Table
public class Article {

    @Name
    private String wos;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String title;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String author;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String authorFullname;

    @Column
    private String type;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String source;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String conference;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String abstractt;

    @Column
    @ColDefine(type = ColType.TEXT)
    private String keyword;

    @Column
    private int year;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorFullname() {
        return authorFullname;
    }

    public void setAuthorFullname(String authorFullname) {
        this.authorFullname = authorFullname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getAbstractt() {
        return abstractt;
    }

    public void setAbstractt(String abstractt) {
        this.abstractt = abstractt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Article{" +
                "wos='" + wos + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
