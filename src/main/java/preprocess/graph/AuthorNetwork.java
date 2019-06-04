package preprocess.graph;

import com.likole.aihw.bean.Article;
import com.likole.aihw.bean.Author;
import preprocess.utils.DbUtils;
import preprocess.utils.NeoUtils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.nutz.json.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author likole
 */
public class AuthorNetwork {

    public void processNodes() {
        List<Author> authors = DbUtils.getDao().query(Author.class, null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (Author author : authors) {
                Node s = NeoUtils.db().createNode(NeoUtils.NodeTypes.AUTHOR);
                s.setProperty("name", author.getAuthorFullName());
                s.setProperty("shortName", author.getAuthorShortName());
                s.setProperty("nums", author.getArticleNumbers());
                s.setProperty("year", author.getFirstYear());
            }
            tx.success();
        }
    }

    public void processRelationships() {
        List<Article> articles = DbUtils.getDao().query(Article.class, null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (Article article : articles) {
                List<String> authors = (List<String>) Json.fromJson(article.getAuthorFullname());
                if (authors == null || authors.size() <= 1) {
                    continue;
                }
                Node a = NeoUtils.db().findNode(NeoUtils.NodeTypes.AUTHOR, "name", authors.get(0));
                for (int i = 1; i < authors.size(); i++) {
                    try {
                        Node b = NeoUtils.db().findNode(NeoUtils.NodeTypes.AUTHOR, "name", authors.get(i));
                        Relationship r = a.createRelationshipTo(b, NeoUtils.RelTypes.COOPERATE);
                        r.setProperty("year", article.getYear());
                    } catch (Exception e) {
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

    public void processRelationships2() {
        List<Article> articles = DbUtils.getDao().query(Article.class, null);
        try (Transaction tx = NeoUtils.db().beginTx()) {
            for (Article article : articles) {
                List<String> authors = (List<String>) Json.fromJson(article.getAuthorFullname());
                if (authors == null || authors.size() <= 1) {
                    continue;
                }
                for (int i = 0; i < authors.size(); i++) {
                    for (int j = i + 1; j < authors.size(); j++) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("a", authors.get(i));
                        params.put("b", authors.get(j));
                        params.put("c", article.getTitle());
                        NeoUtils.db().execute("MATCH (a:AUTHOR{name:$a}),(b:AUTHOR{name:$b}) MERGE (a)-[r:COOPERATE{article:$c}]->(b)", params);
                    }
                }
            }
            tx.success();
        }
    }

}
