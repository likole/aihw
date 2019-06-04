package com.likole.aihw.module;

import com.kennycason.kumo.WordFrequency;
import com.likole.aihw.bean.Article;
import com.likole.aihw.bean.ArticleReference;
import com.likole.aihw.dto.WordCloudDto;
import com.likole.aihw.utils.WordCloudUtils;
import org.neo4j.driver.v1.*;
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

import static org.neo4j.driver.v1.Values.parameters;

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
                article.setKeywords((List<String>) Json.fromJson(article.getKeyword()));
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
            article.setKeywords((List<String>) Json.fromJson(article.getKeyword()));
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

    @At("/cp")
    public Object couping(@Param("wos")String wos){
        List<Article> coupings=new ArrayList<>();
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j","397032663"));
        try(Session session = driver.session()){
            StatementResult result = session.run("match (:ARTICLE{wos:{WOS}})-[:COUPING_FIELD]->(a)-[r]->(b) return b.wos as wos,b.title as title",
                    parameters("WOS",wos));
            while (result.hasNext()){
                Record record=result.next();
                Article article=new Article();
                article.setWos(record.get("wos").asString());
                article.setTitle(record.get("title").asString());
                coupings.add(article);
            }
        }
        driver.close();
        return new NutMap().setv("code",0).setv("coupings",coupings);
    }

    @At("/cc")
    public Object cocitition(@Param("wos")String wos){
        List<Article> coCitation=new ArrayList<>();
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j","397032663"));
        try(Session session = driver.session()){
            StatementResult result = session.run("match (:ARTICLE{wos:{WOS}})-[:CO_CITATION_FIELD]->(a)-[r]->(b) return b.wos as wos,b.title as title",
                    parameters("WOS",wos));
            while (result.hasNext()){
                Record record=result.next();
                Article article=new Article();
                article.setWos(record.get("wos").asString());
                article.setTitle(record.get("title").asString());
                coCitation.add(article);
            }
        }
        driver.close();
        return new NutMap().setv("code",0).setv("citations",coCitation);
    }

    @At("/ct")
    public Object cotext(@Param("wos")String wos){
        List<Article> cotexts=new ArrayList<>();
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j","397032663"));
        try(Session session = driver.session()){
            StatementResult result = session.run("match (:ARTICLE{wos:{WOS}})-[:CO_TEXT_FIELD]->(a)-[r]->(b) return b.wos as wos,b.title as title",
                    parameters("WOS",wos));
            while (result.hasNext()){
                Record record=result.next();
                Article article=new Article();
                article.setWos(record.get("wos").asString());
                article.setTitle(record.get("title").asString());
                cotexts.add(article);
            }
        }
        driver.close();
        return new NutMap().setv("code",0).setv("cotexts",cotexts);
    }
}
