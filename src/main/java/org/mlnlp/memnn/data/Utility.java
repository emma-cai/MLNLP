package org.mlnlp.memnn.data;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Maochen on 9/3/15.
 */
public class Utility {

    public static List<MemnetTuple> readFile(String filename) {
        List<MemnetTuple> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();

            while (line != null) {
                line = line.trim();
                int id = Integer.parseInt(line.split("\\s")[0]);
                MemnetTuple tuple = new MemnetTuple();
                tuple.id = id;
                String[] sentences = line.split("\t")[0].trim().split("\\s");

                tuple.sentence = IntStream.range(1, sentences.length).mapToObj(i -> sentences[i])
                        .reduce((x1, x2) -> x1 + StringUtils.SPACE + x2).get();

                if (line.split("\t").length > 1) {
                    String[] extra = line.split("\t");
                    tuple.shortAnswer = extra[1];
                    tuple.refId = Arrays.stream(extra[2].split("\\s")).mapToInt(Integer::parseInt).toArray();
                }
                data.add(tuple);

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void main(String[] args) {
        String filename = "/Users/Maochen/Desktop/simpledata/en-10k/qa1_single-supporting-fact_train.txt";
        List<MemnetTuple> result = readFile(filename);

        result.forEach(System.out::println);
    }
}
