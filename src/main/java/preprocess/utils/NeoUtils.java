package preprocess.utils;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class NeoUtils {


    private static GraphDatabaseService graphDb;

    public enum NodeTypes implements Label {
        AUTHOR,ARTICLE,COUPLING_CENTER,CO_CITATION_CENTER,COUPLING_FIELD,CO_CITATION_FIELD,COUPLING,CO_CITATION,COOPERATE_CENTER
    }

    public enum RelTypes implements RelationshipType {
        COOPERATE,COOPERATE_FIELD
    }


    private static void setHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static GraphDatabaseService db() {
        if (graphDb == null) {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("db/databases/graph.db"));
            setHook(graphDb);
        }
        return graphDb;
    }

    public void start() {
        try (Transaction tx = graphDb.beginTx()) {

            Node firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");
            Node secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            Relationship relationship = firstNode.createRelationshipTo(secondNode, RelTypes.COOPERATE);
            relationship.setProperty("message", "brave Neo4j ");
            tx.success();
        }
    }
}
