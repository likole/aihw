package com.likole.aihw.preprocess.graph;

import com.likole.aihw.bean.ArticleReference;
import com.likole.aihw.preprocess.DbUtils;
import com.likole.aihw.preprocess.NeoUtils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceNetwork {
    public void processNodes() {
        List<ArticleReference> reference = DbUtils.getDao().query(ArticleReference.class, null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (ArticleReference articleReference : reference) {
                Map<String, Object> params = new HashMap<>();
                params.put("wos", articleReference.getFromWOS());
                params.put("title", articleReference.getFromTitle());
                NeoUtils.db().execute("merge (a:ARTICLE{wos:$wos,title:$title})", params);
                params.put("wos", articleReference.getToWOS());
                params.put("title", articleReference.getToTitle());
                NeoUtils.db().execute("merge (a:ARTICLE{wos:$wos,title:$title})", params);
            }
            tx.success();
        }
    }

    public void processRelationships() {
        Sql sql = Sqls.create("SELECT DISTINCT fromWOS FROM article_references");
        sql.setCallback(Sqls.callback.strList());
        DbUtils.getDao().execute(sql);
        List<String> froms = sql.getList(String.class);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (String from : froms) {
                List<ArticleReference> tos = DbUtils.getDao().query(ArticleReference.class, Cnd.where("fromWos", "=", from));
                // from -> tos.get(i).getToWos
                for (int i = 0; i < tos.size(); i++) {
                    Node a = NeoUtils.db().findNode(NeoUtils.NodeTypes.ARTICLE, "toWOS", tos.get(i));
                    for (int j = i + 1; i < tos.size(); j++) {
                        try {
                            Node b = NeoUtils.db().findNode(NeoUtils.NodeTypes.AUTHOR, "toWOS", tos.get(j));
                            Relationship r = a.createRelationshipTo(b, NeoUtils.RelTypes.COOPERATE);
                            //r.setProperty("fromWOS", reference_new.getFromWOS());
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  System.out.println(reference_new);
                            System.out.println(tos.get(i));
                        }
                    }
                    //   System.out.println(reference_new.getFromWOS());
                }
                tx.success();
            }
        }
    }

    public void processRelationships2() {
        Sql sql = Sqls.create("SELECT DISTINCT fromWOS FROM article_reference");
        sql.setCallback(Sqls.callback.strList());
        DbUtils.getDao().execute(sql);
        List<String> froms = sql.getList(String.class);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (String from : froms) {
                List<ArticleReference> tos = DbUtils.getDao().query(ArticleReference.class, Cnd.where("fromWOS", "=", from));
                for (int i = 0; i < tos.size(); i++) {
                    for (int j = i + 1; j < tos.size(); j++) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("a", tos.get(i).getToWOS());
                        params.put("b", tos.get(j).getToWOS());
                        params.put("c", tos.get(i).getFromTitle());
                        NeoUtils.db().execute("MATCH (a:ARTICLE{wos:$a}),(b:ARTICLE{wos:$b}) MERGE (a)-[r:CO_CITATION{title:$c}]->(b)", params);
                    }
                }
            }
            tx.success();
        }
    }

}
