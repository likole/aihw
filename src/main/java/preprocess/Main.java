package preprocess;

import preprocess.graph.AuthorNetwork;
import preprocess.graph.ReferenceNetwork;
import preprocess.spider.article.Parser;

import java.io.IOException;

/**
 * @author likole
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.parse("http://127.0.0.1:8082/static/1.html");
        parser.parse("http://127.0.0.1:8082/static/2.html");
        parser.parse("http://127.0.0.1:8082/static/3.html");
//        AuthorNetwork authorNetwork = new AuthorNetwork();
//        authorNetwork.processNodes();
//        authorNetwork.processRelationships2();
//        ReferenceNetwork referenceNetwork = new ReferenceNetwork();
//        referenceNetwork.processNodes();
//        referenceNetwork.processRelationships2();
//        referenceNetwork.processRelationships3();
    }
}
