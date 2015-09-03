//package org.mlnlp.corpus.penntreebank.nnparser;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by qingqingcai on 8/27/15.
// */
//public class DataConverter {
//    public static void main(String[] args) {
//        String pennDirectory = "/Users/qingqingcai/Documents/data/NNDep/lht_converter/PennTreeBank";
//
//        try {
//            List<String> pathList = new ArrayList<>();
//            collectFiles(pennDirectory, pathList);
//            pathList.stream().forEach(path -> {
//                System.out.println("\nprocessing " + path);
//                String outputPath = path.replace("PennTreeBank", "ConllxTreeBank");
//                File outputFile = new File(outputPath);
//                if(!outputFile.exists()) {
//                    outputFile.mkdir();
//                }
//                PennConverter.main(new String[]{
//                        "-rightBranching=false",
//                        "-f", path,
//                        "-t", outputPath
//                });
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void collectFiles(String sDir, List<String> pathList){
//
//        File[] faFiles = new File(sDir).listFiles();
//        for(File file: faFiles){
//            if(file.getName().endsWith(".mrg")){
//                pathList.add(file.getAbsolutePath());
//            }
//            if(file.isDirectory()){
//                collectFiles(file.getAbsolutePath(), pathList);
//            }
//        }
//    }
//
//}
