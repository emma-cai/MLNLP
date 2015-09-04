package org.mlnlp.memnn.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Maochen on 9/3/15.
 */
public class TrainingDataMatrix {
    public List<MemnetTuple> trainingData;

//    public BiMap<String, Integer> wordIndexer = HashBiMap.create();
    // trainingDataIndex, Index in matrix (row)
    public BiMap<Integer, Integer> trainingSampleIndexer = HashBiMap.create();

    public List<String> matrixCol;
    public double[][] matrix;

    public void load(String trainingDataFile) {
        trainingData = Utility.readFile(trainingDataFile);

        Set<String> words = trainingData.parallelStream().map(mt -> {
            String[] tokens = mt.sentence.replaceAll("\\p{Punct}+$", StringUtils.EMPTY).split("\\s");
            return Arrays.stream(tokens).map(String::toLowerCase).collect(Collectors.toSet());
        }).reduce((s1, s2) -> {
            Set<String> reduced = new HashSet<>(s1);
            reduced.addAll(s2);
            return reduced;
        }).get();

        matrixCol = new ArrayList<>(words);

//        int ct = 0;
//        for (String word : words) {
//            wordIndexer.put(word, ct++);
//        }

        matrix = new double[trainingData.size()][words.size()];

        IntStream.range(0, trainingData.size()).mapToObj(i -> {
            String[] tokens = trainingData.get(i).sentence.replaceAll("\\p{Punct}+$", StringUtils.EMPTY).split("\\s");

            Map<String, Long> count = Arrays.stream(tokens)
                    .map(String::toLowerCase)
                    .map(x -> new AbstractMap.SimpleImmutableEntry<>(x, 1))
                    .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, Collectors.counting()));

            double[] entry = new double[words.size()];

            for (int j = 0; j < matrixCol.size(); j++) {
                entry[j] = count.containsKey(matrixCol.get(j)) ? count.get(matrixCol.get(j)) : 0;
            }

            matrix[i] = entry;
            trainingSampleIndexer.put(trainingData.get(i).id, i);
            return null;
        }).collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        String filename = "/Users/qingqingcai/Desktop/simpledata/en-10k/qa1_single-supporting-fact_train.txt";

        TrainingDataMatrix tdm = new TrainingDataMatrix();
        tdm.load(filename);

        System.out.println(tdm.matrixCol.toString());
        for (int i = 0; i < tdm.matrix.length; i++) {
            int id = tdm.trainingSampleIndexer.inverse().get(i);
            String sentence = tdm.trainingData.stream().filter(t -> t.id == id).map(x -> x.sentence).findFirst().orElse(null);
            System.out.print(sentence + StringUtils.SPACE);
            for (int j = 0; j < tdm.matrix[i].length; j++) {
                System.out.print(tdm.matrix[i][j] + StringUtils.SPACE);
            }
            System.out.println(StringUtils.EMPTY);
        }

    }
}
