package org.mlnlp.nlp.answerextraction.caching;

import org.junit.Test;
import org.maochen.nlp.datastructure.DTree;

import java.io.File;

/**
 * Created by qingqingcai on 8/7/15.
 */
public class DataCacherTest {

    private static final String DATAPATH = System.getProperty("user.home") + File.separator + "CorpusTestDataCache";
    private static final String DATA_FILENAME = "parses";
    public static final DataCacher DATA_CACHER = new H2DataCacher(DATAPATH, DATA_FILENAME);

    @Test
    public void test1() {

        String corpusName = "AbrahamLincoln.txt";
        String actualLongAnswer = "Lincoln set the slaves free in 1863.";
        String query = "When were the slaves set free?";
//        List<String> statements = CACHED_PASSAGE.get(corpusName);
//        statements.forEach(s -> System.out.println(s));
        // Short answer extraction test
        DTree textDTree = DATA_CACHER.parseSentence(actualLongAnswer);
        DTree questionDTree = DATA_CACHER.parseSentence(query);
        System.out.println(textDTree);
        System.out.println();
        System.out.println(questionDTree);

    }
}
