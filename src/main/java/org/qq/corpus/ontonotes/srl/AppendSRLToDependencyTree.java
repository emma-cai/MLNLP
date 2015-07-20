package org.qq.corpus.ontonotes.srl;


import org.maochen.nlp.datastructure.DTree;

import java.io.File;
import java.util.Map;

/**
 * Created by qingqingcai on 7/20/15.
 */
public class AppendSRLToDependencyTree {

//    public static Map<Integer, DTree> addSRLToConllx(Map<Integer, DTree> trees, Table<Integer, Integer, Map<String, String>> infos) {
//        Map<Integer, DTree> result = new HashMap<>();
//        // Append feats and SRL to original conllx
//        trees.entrySet().stream().forEach(e -> result.put(e.getKey(), addSRLToConllx(e.getValue(), infos.row(e.getKey()))));
//        return result;
//    }
//
//    public static DTree addSRLToConllx(DTree tree, Map<Integer, Map<String, String>> info) {
//        info.entrySet().stream().forEach(e -> {
//            tree.get(e.getKey()).addFeature(e.getValue().get("???????"));
//            tree.get(e.getKey()).addSemanticHead("??????");
//
//        });
//        return tree;
//    }

    public static void forEachFile(String inputConllxDirectory, String inputPropDirectory, String outputDirectory) {

        File theConllxDir = new File(inputConllxDirectory);
        File[] fList = theConllxDir.listFiles();
        for (File file : fList) {
            String absolutePath = file.getAbsolutePath();
            if (file.isFile()) {
                String inputConllxFilePath = file.getPath();
                String inputPropFilePath = inputPropDirectory
                        + inputConllxFilePath.substring(inputConllxFilePath.indexOf("dependencyTree") + "dependencyTree".length(), inputConllxFilePath.lastIndexOf(File.separator))
                        + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."))  + ".prop";
                String outputFilePath = outputDirectory + inputConllxFilePath.substring(inputConllxFilePath.indexOf("dependencyTree") + "dependencyTree".length());

                if (file.getName().endsWith(".parse")) {
                    System.out.println("inputConllxFilePath = " + inputConllxFilePath);
                    System.out.println("inputPropFilePath = " + inputPropFilePath);
                    System.out.println("outputFilePath = " + outputFilePath);
                    System.out.println();
                    readTrees(inputConllxFilePath);
                }
            } else if (file.isDirectory()) {
                forEachFile(absolutePath, inputPropDirectory, outputDirectory);
            }
        }



//        Map<Integer, DTree> trees = readTrees(inputConllxDirectory);
//        Table<Integer, Integer, Map<String, String>> infos = readInfos(inputPropDirectory);
//        Map<Integer, DTree> treesWithSRL = addSRLToConllx(trees, infos);
//        // write to file????
    }

    private static Map<Integer, DTree> readTrees(String inputFilePath) {
        return null;
    }

    public static void main(String[] args) {
        String inputConllxDirectory = "data/ontonotes/dependencyTree";
        String inputPropDirectory = "/Users/qingqingcai/Documents/ontonotes-release-5.0/data/files/data";
        String outputDirectory = "data/ontonotes/dependencySRLTree";
        forEachFile(inputConllxDirectory, inputPropDirectory, outputDirectory);
    }
}
