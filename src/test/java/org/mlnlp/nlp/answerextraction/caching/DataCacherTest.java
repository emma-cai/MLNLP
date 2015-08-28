//package org.mlnlp.nlp.answerextraction.caching;
//
//import org.junit.AfterClass;
//import org.junit.Test;
//import org.maochen.nlp.datastructure.DTree;
//
//import java.io.File;
//
///**
// * Created by qingqingcai on 8/7/15.
// */
//public class DataCacherTest {
//
//    private static final String DATAPATH = System.getProperty("user.home") + File.separator + "CorpusTestDataCache";
//    private static final String DATA_FILENAME = "parses";
//
//    public static final DataCacher DATA_CACHER = new H2DataCacher(DATAPATH, DATA_FILENAME);
//
//
//    @AfterClass
//    public static void closeCacher() {
//        DATA_CACHER.close();
//    }
//
//    @Test
//    public void runTest() {
//
//
//        // Short answer extraction test
//    //    String sentence = "Lincoln set the slaves free in 1863\\.";
//        String question = "What is a Galaxy?";
//    //    DTree textDTree = DATA_CACHER.parseSentence(sentence);
//        DTree questionDTree = DATA_CACHER.parseSentence(question);
//    //    System.out.println(textDTree);
//        System.out.println(questionDTree);
//
//    }
//}
