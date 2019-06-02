package com.likole.aihw.module;


import com.likole.aihw.bean.Author;
import com.likole.aihw.bean.AuthorArticle;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Param;

import java.util.List;

@IocBean
@At("/author")
public class AuthorModule {

    @Inject
    private Dao dao;

    @At("/")
    public Object getAuthors(){
        NutMap re=new NutMap();
        return new NutMap().setv("code",0).setv("authors",dao.query(Author.class,null));
    }

    @At("/f")
    public Object getAuthor(@Param("fn")String fullName){
        NutMap re=new NutMap();
        Author author= dao.fetch(Author.class,fullName);
        if(author==null) {
            return re.setv("code",1);
        }
        List<AuthorArticle> authorArticles=dao.query(AuthorArticle.class, Cnd.where("authorFullName","=",fullName));
        return re.setv("code",0).setv("author",author).setv("articles",authorArticles);
    }
}
