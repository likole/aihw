package com.likole.aihw.preprocess.graph;

import com.likole.aihw.bean.Article;
import com.likole.aihw.bean.Author;
import com.likole.aihw.preprocess.DbUtils;
import com.likole.aihw.preprocess.NeoUtils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.nutz.json.Json;

import java.util.List;

public class AuthorNetwork {

    public void processNodes(){
        List<Author> authors= DbUtils.getDao().query(Author.class,null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (Author author:authors){
                Node s=NeoUtils.db().createNode(NeoUtils.NodeTypes.AUTHOR);
                s.setProperty("name",author.getAuthorFullName());
                s.setProperty("shortName",author.getAuthorShortName());
                s.setProperty("nums",author.getArticleNumbers());
                s.setProperty("year",author.getFirstYear());
            }
            tx.success();
        }
    }

    public void processRelationships(){
        List<Article> articles=DbUtils.getDao().query(Article.class,null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (Article article:articles){
                List<String> authors= (List<String>) Json.fromJson(article.getAuthorFullname());
                if(authors==null||authors.size()<=1) {
                    continue;
                }
                Node a=NeoUtils.db().findNode(NeoUtils.NodeTypes.AUTHOR,"name",authors.get(0));
                for(int i=1;i<authors.size();i++){
                    try {
                        Node b=NeoUtils.db().findNode(NeoUtils.NodeTypes.AUTHOR,"name",authors.get(i));
                        Relationship r=a.createRelationshipTo(b,NeoUtils.RelTypes.COOPERATE);
                        r.setProperty("year",article.getYear());
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println(article);
                        System.out.println(authors.get(i));
                    }
                }
                System.out.println(article.getWos());
            }
            tx.success();
        }
    }


    public static void main(String[] args) {
        AuthorNetwork authorNetwork=new AuthorNetwork();
        authorNetwork.processRelationships();
    }
}
