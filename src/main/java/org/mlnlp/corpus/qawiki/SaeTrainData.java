package org.mlnlp.corpus.qawiki;

import org.maochen.nlp.parser.DTree;
import org.maochen.nlp.parser.LangTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingqingcai on 8/25/15.
 */
public class SaeTrainData {

    public static void main(String[] args) {
        String filePath = "";
        List<String> queList = new ArrayList<>();
        List<String> queConllxList = new ArrayList<>();
        List<String> senList = new ArrayList<>();
        List<String> senConllxList = new ArrayList<>();

        for (int i = 0; i < queList.size(); i++) {
            String question = queList.get(i);
            String sentence = senList.get(i);

            String questionConllx = queConllxList.get(i);
            DTree questionTree = LangTools.getDTreeFromCoNLLXString(questionConllx);


            // put question, sentence and answer into json or txt file.
            // need to be discussed.
        }
    }
}
