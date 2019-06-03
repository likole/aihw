package com.likole.aihw.module;

import com.kennycason.kumo.WordFrequency;
import com.likole.aihw.bean.Article;
import com.likole.aihw.bean.ArticleReference;
import com.likole.aihw.dto.WordCloudDto;
import com.likole.aihw.utils.WordCloudUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@At("/article")
@IocBean
public class ArticleModule {

    @Inject
    private Dao dao;

    @At("/list")
    public Object getArticles(@Param("page") int page) {
        NutMap re = new NutMap();
        Pager pager = dao.createPager(page, 5);
        List<Article> articles = dao.query(Article.class, null, pager);
        pager.setRecordCount(dao.count(Article.class));
        for (Article article : articles) {
            //keywords
            if (article.getKeyword() == null) {
                article.setKeywords(new ArrayList<String>());
            } else {
                List<String> a = (List<String>) Json.fromJson(article.getKeyword());
                if (a == null || a.size() == 0) {
                    article.setKeywords(new ArrayList<String>());
                } else {
                    article.setKeywords(Arrays.asList(a.get(0).split("; ")));
                }
            }
            //author names
            if (article.getAuthorFullname() == null) {
                article.setAuthorFullNames(new ArrayList<String>());
            } else {
                article.setAuthorFullNames((List<String>) Json.fromJson(article.getAuthorFullname()));
            }
        }
        return re.setv("code", 0).setv("articles", articles).setv("pager", pager);
    }

    @At("/detail")
    public Object getDetail(@Param("wos") String wos) {
        NutMap re = new NutMap();
        Article article = dao.fetch(Article.class, wos);
        //author names
        if (article.getAuthorFullname() == null) {
            article.setAuthorFullNames(new ArrayList<String>());
        } else {
            article.setAuthorFullNames((List<String>) Json.fromJson(article.getAuthorFullname()));
        }
        //keywords
        if (article.getKeyword() == null) {
            article.setKeywords(new ArrayList<String>());
        } else {
            List<String> a = (List<String>) Json.fromJson(article.getKeyword());
            if (a == null || a.size() == 0) {
                article.setKeywords(new ArrayList<String>());
            } else {
                article.setKeywords(Arrays.asList(a.get(0).split("; ")));
            }
        }
        //wordcloud
        List<WordFrequency> wordFrequencies= WordCloudUtils.frequence(article.getAbstractt(),article.getKeywords());
        List<WordCloudDto> wordCloudDtos=new ArrayList<>();
        for (WordFrequency wordFrequency:wordFrequencies){
            WordCloudDto wordCloudDto=new WordCloudDto();
            wordCloudDto.setName(wordFrequency.getWord());
            wordCloudDto.setValue(wordFrequency.getFrequency());
            wordCloudDtos.add(wordCloudDto);
        }
        return re.setv("code", 0).setv("article", article).setv("wc",wordCloudDtos);
    }

    @At("/rs")
    public Object references(@Param("wos")String wos){
        NutMap re=new NutMap();
        List<ArticleReference> tos=dao.query(ArticleReference.class, Cnd.where("fromWOS","=",wos));
        List<ArticleReference> froms=dao.query(ArticleReference.class, Cnd.where("toWOS","=",wos));
        return re.setv("code",0).setv("tos",tos).setv("froms",froms);
    }
}
