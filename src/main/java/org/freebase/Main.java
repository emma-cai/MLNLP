package org.freebase;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by qingqingcai on 12/1/15.
 */
public class Main {

    private static int maxHits = 10;

    public static void main(String[] args) {
        String inputPath = "/Users/qingqingcai/Downloads/freebase-rdf-latest.gz";
        String indexDirPath = "/Users/qingqingcai/Downloads/index/freebase-simple-indexer.idx";

    //    Indexer.buildIndex(inputPath, indexDirPath);

    //    runSearchFromConsole(indexDirPath);
    //    runMultiSearchFromConsole(indexDirPath);
    //    getTypeByName(indexDirPath);
        getInstanceByType(indexDirPath);
    }

    public static void getTypeByName(String indexDirPath) {
        Searcher searcher = new Searcher(indexDirPath);

        try {
            String queryString = StringUtils.EMPTY;
            do {
                System.out.print("\nQuery: > ");
                queryString = new Scanner(System.in).nextLine();

                // Operator AND
                TopDocs firstRoundSearchHits = searcher.search("arg2", queryString, maxHits);

                for (ScoreDoc firstScoreDoc : firstRoundSearchHits.scoreDocs) {
                    Document firstRoundDocument = searcher.indexSearcher.doc(firstScoreDoc.doc);
                    TopDocs secondRoundSearchHits = searcher.multiSearch(
                            "arg1", firstRoundDocument.get("arg1"),
                            "predicate", "type.object.type", maxHits);

                    for (ScoreDoc secondScoreDoc : secondRoundSearchHits.scoreDocs) {
                        Document secondRoundDocument = searcher.indexSearcher.doc(secondScoreDoc.doc);
                        System.out.println("-----");
                        System.out.println("firstRound / mid = " + firstRoundDocument.get("arg1"));
                        System.out.println("firstRound / predicate = " + firstRoundDocument.get("predicate"));
                        System.out.println("firstRound / name = " + firstRoundDocument.get("arg2"));
                        System.out.println("secondRound / mid = " + secondRoundDocument.get("arg1"));
                        System.out.println("secondRound / predicate = " + secondRoundDocument.get("predicate"));
                        System.out.println("secondRound / type = " + secondRoundDocument.get("arg2"));
                    }
                }

            } while (!queryString.matches("exit|quit|q"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        searcher.tearDown();
    }

    public static void runSearchFromConsole(String indexDirPath) {
        Searcher searcher = new Searcher(indexDirPath);

        try {
            String fieldString = StringUtils.EMPTY;
            String queryString = StringUtils.EMPTY;
            do {
                System.out.print("\nField: > ");
                fieldString = new Scanner(System.in).nextLine();
                System.out.print("\nQuery: > ");
                queryString = new Scanner(System.in).nextLine();

                // Operator AND
                TopDocs topDocs = searcher.search(fieldString, queryString, 10);

                System.out.println("total hits = " + topDocs.totalHits);
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    Document document = searcher.indexSearcher.doc(scoreDoc.doc);
                    System.out.println("-----");
                    System.out.println("arg1 = " + document.get("arg1"));
                    System.out.println("predicate = " + document.get("predicate"));
                    System.out.println("arg2 = " + document.get("arg2"));
                }

            } while (!queryString.matches("exit|quit|q"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        searcher.tearDown();
    }

    public static void runMultiSearchFromConsole(String indexDirPath) {
        Searcher searcher = new Searcher(indexDirPath);

        try {
            String queryString1 = StringUtils.EMPTY;
            String queryString2 = StringUtils.EMPTY;
            do {
                System.out.print("\nqueryString1: > ");
                queryString1 = new Scanner(System.in).nextLine();
                System.out.print("\nqueryString2: > ");
                queryString2 = new Scanner(System.in).nextLine();

                // Operator AND
                TopDocs topDocs = searcher.multiSearch(
                        "arg1", queryString1, "predicate", queryString2, maxHits);

                System.out.println("total hits = " + topDocs.totalHits);
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    Document document = searcher.indexSearcher.doc(scoreDoc.doc);
                    System.out.println("-----");
                    System.out.println("arg1 = " + document.get("arg1"));
                    System.out.println("predicate = " + document.get("predicate"));
                    System.out.println("arg2 = " + document.get("arg2"));
                }

            } while (!queryString1.matches("exit|quit|q"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        searcher.tearDown();
    }

    public static void getInstanceByType(String indexDirPath) {

        Searcher searcher = new Searcher(indexDirPath);

        try {
            String queryString = StringUtils.EMPTY;
            do {
                System.out.print("\nFreebase Type: > ");
                queryString = new Scanner(System.in).nextLine();

                // Operator AND
                TopDocs firstRoundSearchHits = searcher.search("arg2", queryString, maxHits);

                for (ScoreDoc firstScoreDoc : firstRoundSearchHits.scoreDocs) {
                    Document firstRoundDocument = searcher.indexSearcher.doc(firstScoreDoc.doc);
                    TopDocs secondRoundSearchHits = searcher.multiSearch(
                            "arg1", firstRoundDocument.get("arg1"),
                            "predicate", "type.object.name", maxHits);

                    for (ScoreDoc secondScoreDoc : secondRoundSearchHits.scoreDocs) {
                        Document secondRoundDocument = searcher.indexSearcher.doc(secondScoreDoc.doc);
                        System.out.println("-----");
                        System.out.println("firstRound / mid = " + firstRoundDocument.get("arg1"));
                        System.out.println("firstRound / predicate = " + firstRoundDocument.get("predicate"));
                        System.out.println("firstRound / type = " + firstRoundDocument.get("arg2"));
                        System.out.println("secondRound / mid = " + secondRoundDocument.get("arg1"));
                        System.out.println("secondRound / predicate = " + secondRoundDocument.get("predicate"));
                        System.out.println("secondRound / name = " + secondRoundDocument.get("arg2"));
                    }
                }

            } while (!queryString.matches("exit|quit|q"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        searcher.tearDown();
    }

}
