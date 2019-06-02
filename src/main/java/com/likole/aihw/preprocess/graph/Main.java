package com.likole.aihw.preprocess.graph;

/**
 * @author likole
 */
public class Main {

    public static void main(String[] args) {
        AuthorNetwork authorNetwork = new AuthorNetwork();
//        authorNetwork.processNodes();
//        authorNetwork.processRelationships2();
        ReferenceNetwork referenceNetwork = new ReferenceNetwork();
//        referenceNetwork.processNodes();
        referenceNetwork.processRelationships2();
    }
}
