package org.freebase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by qingqingcai on 11/25/15.
 */
public class Indexer {

    // THIS IS THE MAXIMUM LINE WE CAN READ NOW.
    private static int MAXLINE = 53570200;

    public Indexer() {

    }

    public static void buildIndex(String inputPath, String indexDirPath) {

        BufferedReader in = null;
        try {

            // Read data from GZIP file
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputPath));
            in = new BufferedReader(new InputStreamReader(gzipInputStream));

            // Prepare for indexing
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
            Directory indexDir = FSDirectory.open(new File(indexDirPath));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_42, analyzer);
            IndexWriter indexWriter = new IndexWriter(indexDir, indexWriterConfig);
            indexWriter.deleteAll();

            // read each line, add it for indexing if it is one of bhi's interests
            String content;
            int stopline = 1;
            while ((content = in.readLine()) != null && stopline <= MAXLINE ) {

                String[] contentArray = content.split("\t");
                String arg1String = contentArray[0].substring(contentArray[0].lastIndexOf("/") + 1, contentArray[0].length() - 1);
                String predString = contentArray[1].substring(contentArray[1].lastIndexOf("/") + 1, contentArray[1].length() - 1);


                if (contentArray[0].contains("m.0dmgv9n") && contentArray[2].contains("San Marino")) {
                    System.out.println("debug for = " + contentArray[2]);
                }

                int content2StartIndex = contentArray[2].lastIndexOf("/") == -1 ? 0 : contentArray[2].lastIndexOf("/");
                boolean isEnglishName = contentArray[2].lastIndexOf("@en") != -1;
                int content2EndIndex = !isEnglishName ? contentArray[2].length() - 1 : contentArray[2].lastIndexOf("@en") - 1;
                String arg2String = contentArray[2].substring(content2StartIndex + 1, content2EndIndex);

                if (isOneOfOurInterests(predString, arg2String, isEnglishName)) {

//                    System.out.println();
//                    System.out.println("arg1 = " + arg1String);
//                    System.out.println("pred = " + predString);
//                    System.out.println("arg2 = " + arg2String);

                    Document document = new Document();
                    document.add(new TextField("arg1", arg1String, Field.Store.YES));
                    document.add(new TextField("predicate", predString, Field.Store.YES));
                    document.add(new TextField("arg2", arg2String, Field.Store.YES));

                    indexWriter.addDocument(document);
                }

                if (stopline % 100 == 0) {
                    System.out.println("# of legal statements are processed: " + stopline);
                }
                stopline++;
            }

            indexWriter.close();
            indexDir.close();

            gzipInputStream.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static  boolean isOneOfOurInterests(String predicate, String arg2, boolean isEnglishName) {

        switch (predicate) {
//            case "location.postal_code.postal_code":
//            case "location.mailing_address.country":
//            case "location.mailing_address.citytown":
//                return true;

            case "type.object.type":
                TypeEnumForBhi arg2MappedToFreebase = TypeEnumForBhi.fromString(arg2);
                if (arg2.equals("location.country")) {
                    System.out.println();
                    System.out.println(arg2);
                    System.out.println(arg2MappedToFreebase.toString());
                }
                if (arg2MappedToFreebase.isOneOf(
                        TypeEnumForBhi.LOCATION_CITYTOWN,
                        TypeEnumForBhi.LOCATION_COUNTRY,
                        TypeEnumForBhi.LOCATION_POSTAL_CODE,
                        TypeEnumForBhi.LOCATION_US_STATE)) {
                    return true;
                } else {
                    return false;
                }
            case "type.object.name":
                return isEnglishName;
            default:
                return false;
        }
    }

}
