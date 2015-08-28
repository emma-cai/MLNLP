package org.experiments.Jackson;

/**
 * Created by qingqingcai on 8/21/15.
 */
public class ConvertJsonToObject {

//    public static void main(String[] args) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            List<QAWiki> objectList = mapper.readValue(new File("/Users/qingqingcai/Desktop/IRtests.json"),
//                                                       new TypeReference<List<QAWiki>>() { });
//
//            objectList.stream().forEach(obj -> System.out.println("\n" + obj.file + "\n" + obj.query + "\n" + obj.answer));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}

class QAWiki {
    public String file;
    public String query;
    public String answer;
}
