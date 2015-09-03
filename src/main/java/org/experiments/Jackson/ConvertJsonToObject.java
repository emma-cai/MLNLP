package org.experiments.Jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by qingqingcai on 8/21/15.
 */
public class ConvertJsonToObject {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<QAWiki> objectList = mapper.readValue(new File("/Users/qingqingcai/Documents/data/SAEClassifier/test.json"),
                                                       new TypeReference<List<QAWiki>>() { });

            objectList.stream().forEach(obj ->
                    System.out.println("\n" + obj.query +
                            "\n" + obj.answer +
                            "\n" + obj.optimal_answer +
                            "\n" + obj.queryTree +
                            "\n" + obj.sentenceTree));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class QAWiki {
    public String query;
    public String answer;
    public String optimal_answer;
    public String queryTree;
    public String sentenceTree;
}
