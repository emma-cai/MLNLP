//package org.mlnlp.nlp.answerextraction.feature;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// *
// * Created by qingqingcai on 8/7/15.
// */
//public class FeatureFactoryTest {
//
//    private static FeatureFactory featureFactory = new FeatureFactory();
//    private static List<String> queList = new ArrayList<>();
//    private static List<String> queConllxListst = new ArrayList<>();
//    private static List<String> senList = new ArrayList<>();
//    private static List<String> senConllxList = new ArrayList<>();
////    private static Map<String, String> que_DTree_Map = new HashMap<>();
////    private static Map<String, String> sen_DTree_Map = new HashMap<>();
//
//    @BeforeClass
//    public static void setUp() {
//        String cachedFilePath = "/Users/qingqingcai/Downloads/SAEHMM-200.txt";
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(cachedFilePath));
//            String line = bufferedReader.readLine();
//            while (line != null) {
//                String question = line;
//                StringBuilder queStringBuilder = new StringBuilder();
//                while (!(line = bufferedReader.readLine()).isEmpty()) {
//                    queStringBuilder.append(line);
//                    queStringBuilder.append(System.lineSeparator());
//                }
//                String queConllx = queStringBuilder.toString();
//
//                String sentence = bufferedReader.readLine();
//                StringBuilder senStringBuilder = new StringBuilder();
//                while (!(line = bufferedReader.readLine()).isEmpty()) {
//                    senStringBuilder.append(line);
//                    senStringBuilder.append(System.lineSeparator());
//                }
//                String senConllx = senStringBuilder.toString();
//
////                que_DTree_Map.put(question, queConllx);
////                sen_DTree_Map.put(sentence, senConllx);
//                queList.add(question.trim());
//                queConllxList.add(queConllx.trim());
//                senList.add(sentence.trim());
//                senConllxList.add(senConllx.trim());
//
//                line = bufferedReader.readLine();
//            }
//
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExtractFeatures() {
//        int NumericTypeCount = 0;
//        for (int i = 0; i < queList.size(); i++) {
//            String question = queList.get(i);
//            DTree queDTree = LangTools.getDTreeFromCoNLLXString(queConllxList.get(i));
//            String sentence = senList.get(i);
//            DTree senDTree = LangTools.getDTreeFromCoNLLXString(senConllxList.get(i));
//
//            String queType = queDTree.getRoots().get(0).getFeature("question_type");
//            if (queType.startsWith("NUM:")) {
//                System.out.println(question);
//                System.out.println(queDTree);
//                System.out.println(sentence);
//                System.out.println(senDTree);
//                generateTrainingTuple(queDTree, senDTree);
//                System.out.println("\n===================================\n");
//                NumericTypeCount++;
//            }
//        }
//        System.out.println("all question count = " + queList.size());
//        System.out.println("numeric type count = " + NumericTypeCount);
//    }
//
//    public static void generateTrainingTuple(DTree queDTree, DTree senDTree) {
//        List<String> labels = senDTree
//            .stream()
//            .map(n -> n.getFeature("sae_label"))
//            .collect(Collectors.toList());
//        List<Tuple> tupleList = featureFactory.extractFeatures(queDTree, senDTree, labels);
//
//        tupleList.stream().forEach(t -> {
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder
//                .append(t.label)
//                .append("\t");
//            for (int i = 0; i < t.featureName.length; i++) {
//                stringBuilder
//                    .append(t.featureName[i])
//                    .append("=")
//                    .append(t.featureVector[i])
//                    .append(", ");
//            }
//            System.out.println(stringBuilder.toString());
//        });
//    }
//
//    private static String queConllx = "0\t^\t^\t\t\toriginal_sentence=When did Lincoln become the President of the U.S.?|sentence_type=Declarative|uuid=d84fdcde-2f10-4604-85ba-1567f05c8159\tNULL\t\t_\t_\t_\n"
//                                      + "1\tWhen\twhen\tADV\tWRB\t_\t4\tadvmod\t_\t_\t4:R-AM-TMP\n"
//                                      + "2\tdid\tdo\tVERB\tVBD\t_\t4\taux\t_\t_\t_\n"
//                                      + "3\tLincoln\tLincoln\tPROPN\tNNP\tname_entity=Person_start_${3}\t4\tnsubj\t_\t_\t4:A1=PPT\n"
//                                      + "4\tbecome\tbecome\tVERB\tVB\tpb=become.01|question_type=NUM:date\t0\troot\t_\t_\t_\n"
//                                      + "5\tthe\tthe\tDET\tDT\t_\t6\tdet\t_\t_\t_\n"
//                                      + "6\tPresident\tPresident\tPROPN\tNNP\t_\t4\txcomp\t_\t_\t4:A2=PRD\n"
//                                      + "7\tof\tof\tADP\tIN\t_\t6\tprep\t_\t_\t_\n"
//                                      + "8\tthe\tthe\tDET\tDT\t_\t9\tdet\t_\t_\t_\n"
//                                      + "9\tU.S.\tU.S.\tPROPN\tNNP\tname_entity=Location_start_${9}\t7\tpobj\t_\t_\t_\n"
//                                      + "10\t?\t?\tPUNCT\t.\t_\t4\tpunct\t_\t_\t_";
//    private static String senConllx = "0\t^\t^\t\t\toriginal_sentence=Lincoln became president of the United States in 1861.|sentence_type=Declarative|uuid=67c717d9-7f21-47fa-83a2-5f3516f89a8a\tNULL\t\t_\t_\t_\n"
//                                      + "1\tLincoln\tLincoln\tPROPN\tNNP\tname_entity=Person_start_${1}|sae_label=NON_SAE\t2\tnsubj\t_\t_\t2:A1=PPT\n"
//                                      + "2\tbecame\tbecome\tVERB\tVBD\tpb=become.01|sae_label=NON_SAE\t0\troot\t_\t_\t_\n"
//                                      + "3\tpresident\tpresident\tNOUN\tNN\tsae_label=NON_SAE\t2\txcomp\t_\t_\t2:A2=PRD\n"
//                                      + "4\tof\tof\tADP\tIN\tsae_label=NON_SAE\t3\tprep\t_\t_\t_\n"
//                                      + "5\tthe\tthe\tDET\tDT\tsae_label=NON_SAE\t7\tdet\t_\t_\t_\n"
//                                      + "6\tUnited\tUnited\tPROPN\tNNP\tname_entity=Location_start_${6}|sae_label=NON_SAE\t7\tnn\t_\t_\t_\n"
//                                      + "7\tStates\tstate\tPROPN\tNNPS\tname_entity=Location_cont_${6}|sae_label=NON_SAE\t4\tpobj\t_\t_\t_\n"
//                                      + "8\tin\tin\tADP\tIN\tsae_label=SAE\t2\tprep\t_\t_\t2:AM-TMP\n"
//                                      + "9\t1861\t1861\tNUM\tCD\tname_entity=Date_start_${9}|time=1861|sae_label=SAE\t8\tpobj\t_\t_\t_\n"
//                                      + "10\t.\t.\tPUNCT\t.\tsae_label=NON_SAE\t2\tpunct\t_\t_\t_\n";
//}
