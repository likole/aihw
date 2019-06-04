package preprocess.graph;

import com.likole.aihw.bean.ArticleReference;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import preprocess.utils.DbUtils;
import preprocess.utils.NeoUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceNetwork {
    /**
     * 添加论文节点
     */
    public void processNodes() {
        List<ArticleReference> reference = DbUtils.getDao().query(ArticleReference.class, null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (ArticleReference articleReference : reference) {
                Map<String, Object> params = new HashMap<>();
                params.put("wos", articleReference.getFromWOS());
                params.put("title", articleReference.getFromTitle());
                NeoUtils.db().execute("merge (a:ARTICLE{wos:$wos,title:$title})", params);
                NeoUtils.db().execute("match (a:ARTICLE{wos:$wos}) merge (a)-[:COUPING_FIELD]->(:COUPING_CENTER)",params);
                NeoUtils.db().execute("match (a:ARTICLE{wos:$wos}) merge (a)-[:CO_CITATION_FIELD]->(:CO_CITATION_CENTER)",params);
                System.out.println("已插入节点" + articleReference.getFromWOS());
                params.put("wos", articleReference.getToWOS());
                params.put("title", articleReference.getToTitle());
                NeoUtils.db().execute("merge (a:ARTICLE{wos:$wos,title:$title})", params);
                NeoUtils.db().execute("match (a:ARTICLE{wos:$wos}) merge (a)-[:COUPING_FIELD]->(:COUPING_CENTER)",params);
                NeoUtils.db().execute("match (a:ARTICLE{wos:$wos}) merge (a)-[:CO_CITATION_FIELD]->(:CO_CITATION_CENTER)",params);
                System.out.println("已插入节点" + articleReference.getToWOS());
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

    /**
     * 共被引分析
     */
    public void processRelationships2() {
        Sql sql = Sqls.create("SELECT DISTINCT fromWOS FROM article_reference");
        sql.setCallback(Sqls.callback.strList());
        DbUtils.getDao().execute(sql);
        List<String> froms = sql.getList(String.class);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (String from : froms) {
                List<ArticleReference> tos = DbUtils.getDao().query(ArticleReference.class, Cnd.where("fromWOS", "=", from));
                for (int i = 0; i < tos.size(); i++) {
                    for (int j = 0; j < tos.size(); j++) {
                        if(i==j) {
                            continue;
                        }
                        Map<String, Object> params = new HashMap<>();
                        params.put("a", tos.get(i).getToWOS());
                        params.put("b", tos.get(j).getToWOS());
                        params.put("c", tos.get(i).getFromTitle());
                        NeoUtils.db().execute("MATCH (:ARTICLE{wos:$a})-[:CO_CITATION_FIELD]->(a),(b:ARTICLE{wos:$b}) " +
                                "MERGE (a)-[r:CO_CITATION]->(b) " +
                                "on match set r.num=r.num+1 " +
                                "on create set r.num=1", params);
                    }
                }
                System.out.println("已插入共被引" + from);
            }
            tx.success();
        }
    }

    /**
     * 耦合分析
     */
    public void processRelationships3() {
        Sql sql = Sqls.create("SELECT DISTINCT toWOS FROM article_reference");
        sql.setCallback(Sqls.callback.strList());
        DbUtils.getDao().execute(sql);
        List<String> tos = sql.getList(String.class);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (String to : tos) {
                List<ArticleReference> froms = DbUtils.getDao().query(ArticleReference.class, Cnd.where("toWOS", "=", to));
                for (int i = 0; i < froms.size(); i++) {
                    for (int j = 0; j < froms.size(); j++) {
                        if(i==j) {
                            continue;
                        }
                        Map<String, Object> params = new HashMap<>();
                        params.put("a", froms.get(i).getFromWOS());
                        params.put("b", froms.get(j).getFromWOS());
                        params.put("c", froms.get(i).getToTitle());
                        NeoUtils.db().execute("MATCH (:ARTICLE{wos:$a})-[:COUPING_FIELD]->(a),(b:ARTICLE{wos:$b}) " +
                                "MERGE (a)-[r:COUPLING]->(b) " +
                                "on match set r.num=r.num+1 " +
                                "on create set r.num=1", params);
                    }
                }
                System.out.println("已插入耦合" + to);
            }
            tx.success();
        }
    }

}
