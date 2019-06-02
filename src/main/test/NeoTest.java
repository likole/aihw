import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class NeoTest {

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    @Test
    public void start() {
        final GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("src/main/webapp/static/db/databases/graph.db"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
        try (Transaction tx = graphDb.beginTx()) {

            Node firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");
            Node secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            Relationship relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "brave Neo4j ");
            tx.success();
        }

    }
}
