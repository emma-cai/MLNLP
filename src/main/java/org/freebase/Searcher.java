package org.freebase;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.processors.MultiFieldQueryNodeProcessor;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by qingqingcai on 12/1/15.
 */
public class Searcher {

    private static boolean EXACT_MATCH = true;
    protected Directory directory;
    protected IndexReader indexReader;
    protected IndexSearcher indexSearcher;
    protected StandardAnalyzer analyzer;

    public Searcher(String indexDirPath) {
        try {
            directory = FSDirectory.open(new File(indexDirPath));
            indexReader = DirectoryReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
            analyzer = new StandardAnalyzer(Version.LUCENE_42);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        try {
            indexReader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TopDocs multiSearch(String fieldName1, String queryString1,
                               String fieldName2, String queryString2,
                               int maxHits) {

        if (EXACT_MATCH) {
//            queryString1 = String.join("\"", queryString1, "\"");
//            queryString2 = String.join("\"", queryString2, "\"");
            queryString1 = "\"" + queryString1 + "\"";
            queryString2 = "\"" + queryString2 + "\"";
        }

        TopDocs topDocs = null;
        try {
            String[] queries = new String[]{queryString1, queryString2};
            String[] fields = new String[]{fieldName1, fieldName2};
            BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST};

            Query query = MultiFieldQueryParser.parse(Version.LUCENE_42, queries, fields, flags, analyzer);
//            multiFieldQueryParser.setDefaultOperator(QueryParser.Operator.AND);
//            multiFieldQueryParser.setPhraseSlop(0);
//            Query query = MultiFieldQueryParser.parse(Version.LUCENE_42, queries, fields, flags, analyzer);

            topDocs = indexSearcher.search(query, maxHits);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return topDocs;

//        TopDocs topDocs = null;
//        try {
//            MultiFieldQueryNodeProcessor booleanQuery = new BooleanQuery();
//            Query query1 = new TermQuery(new Term(fieldName1, queryString1));
//            Query query2 = new TermQuery(new Term(fieldName2, queryString2));
//            // for AND, using MUST; for OR, using SHOULD
//            booleanQuery.add(query1, BooleanClause.Occur.MUST);
//            booleanQuery.add(query2, BooleanClause.Occur.MUST);
//            topDocs = indexSearcher.search(booleanQuery, maxHits);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return topDocs;
    }

    public TopDocs search(String fieldName, String queryString, int maxHits) {

        if (EXACT_MATCH) {
            queryString = "\"" + queryString + "\"";
        }

        TopDocs topDocs = null;
        try {
            QueryParser queryParser = new QueryParser(Version.LUCENE_42, fieldName, analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            queryParser.setPhraseSlop(0);
            Query query = queryParser.parse(queryString);
            topDocs = indexSearcher.search(query, maxHits);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return topDocs;
    }


//    public static void runSearch() {
//        try {
//            Directory directory = FSDirectory.open(new File("/Users/qingqingcai/Downloads/index/freebase-simple-indexer.idx"));
//            IndexReader indexReader = DirectoryReader.open(directory);
//            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
//
//            String queryString = StringUtils.EMPTY;
//            do {
//                System.out.println();
//                System.out.print("> ");
//                // example, query = "19127", output = "postal_code / city / something else"
//                queryString = new Scanner(System.in).nextLine();
//
//                // Operator AND
//                QueryParser queryParser = new QueryParser(Version.LUCENE_42, "arg2", analyzer);
//                queryParser.setDefaultOperator(QueryParser.Operator.AND);
//                queryParser.setPhraseSlop(0);
//                Query query = queryParser.parse(queryString);
//                TopDocs topDocs = indexSearcher.search(query, maxHits);
//
//                System.out.println("total hits = " + topDocs.totalHits);
//                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                    Document document = indexSearcher.doc(scoreDoc.doc);
//                    System.out.println("-----");
//                    System.out.println("arg1 = " + document.get("arg1"));
//                    System.out.println("predicate = " + document.get("predicate"));
//                    System.out.println("arg2 = " + document.get("arg2"));
//                }
//
//            } while (!queryString.matches("exit|quit|q"));
//
//            indexReader.close();
//            directory.close();
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
