package org.mlnlp.corpus.ontonotes.srl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by qingqingcai on 8/11/15.
 */
public class DataCollection {
    public static void main(String[] args) {
        String path = "data/ontonotes/treeBank/english";
        String outputPath = "data/ontonotes/treeBank/english/DTree-all.txt";
        List<String> rawStringList = new ArrayList<>();
        List<String> conllxStringList = new ArrayList<>();
        try {
            Files.walk(Paths.get(path))
                .filter(f -> f.getFileName().toString().endsWith(".dep"))
                .forEach(f -> {
                    try {
                        Stream<String> lines = Files.lines(Paths.get(f.toString()));
                        final boolean[] start = {true};
                        final StringBuilder[] stringBuilder = {new StringBuilder()};
                        lines.forEach(l -> {
                            if (start[0]) {
                                stringBuilder[0] = new StringBuilder();
                                stringBuilder[0]
                                    .append(l.toString())
                                    .append(System.lineSeparator());
                                start[0] = false;
                            } else if (l.toString().trim().isEmpty()) {
                                rawStringList.add(stringBuilder[0].toString().trim());
                                stringBuilder[0] = new StringBuilder();
                            } else {
                                stringBuilder[0]
                                    .append(l.toString())
                                    .append(System.lineSeparator());
                            }
                        });
                        lines.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        rawStringList.stream().forEach(rawString -> {
            String conllx = convertRawToConllx(rawString);
            conllxStringList.add(conllx + System.lineSeparator());
        });
        System.out.println("size = " + rawStringList.size());

        try {
            Files.write(Paths.get(outputPath), conllxStringList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertRawToConllx(String rawString) {
        String[] lines = rawString.split(System.lineSeparator());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] cols = line.split("\t");
            if (cols.length == 8) {
                stringBuilder
                    .append(cols[0])    // id
                    .append("\t")
                    .append(cols[1])    // form
                    .append("\t")
                    .append(cols[2])    // lemma
                    .append("\t")
                    .append("_")        // cPOSTag
                    .append("\t")
                    .append(cols[3])    // pos
                    .append("\t")
                    .append(cols[4])    // feats
                    .append("\t")
                    .append(cols[5])    // head-id
                    .append("\t")
                    .append(cols[6])    // depLabel
                    .append("\t")
                    .append("_")        // _
                    .append("\t")
                    .append("_")        // _
                    .append("\t")
                    .append(cols[7])    // srl
                    .append(System.lineSeparator());
            }
        }
        return stringBuilder.toString().trim();
    }
}
