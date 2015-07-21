package org.qq.corpus.ontonotes.srl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.stanford.nlp.io.ExtensionFileFilter;
import org.maochen.nlp.datastructure.DNode;
import org.maochen.nlp.datastructure.DTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.qq.corpus.ontonotes.srl.ConstituentTreeToDependencyTreeUtils.*;

/**
 * Created by qingqingcai on 7/20/15.
 */
public class ConstituentTreeToDependencyTree {

    private static final String splitString = " ----- ";
    private static final String TAG = "treeBank";


//    public static void convertTreebankToCoNLLX(String trainDirPath, FileFilter trainTreeBankFilter, String outputFileName,
//                                               Table<Integer, Integer, Map<String, String>> prop) {
//        int counter = 0;
//        DiskTreebank trainTreeBank = new DiskTreebank();
//        trainTreeBank.loadPath(trainDirPath, trainTreeBankFilter);
//
//        try {
//            File theFile = new File(outputFileName);
//            if (!theFile.exists()) {
//                File theDirectory = new File(outputFileName.substring(0, outputFileName.lastIndexOf(File.separator)));
//                theDirectory.mkdirs();
//                theFile.createNewFile();
//            }
//
//            FileWriter fw = new FileWriter(theFile);
//
//            trainTreeBank.stream().forEachOrdered(tree -> {
//                count(counter, trainTreeBank);
//                // preprocee; constituent tree -> dependency tree
//                String preprocessedConstituentTree = preprocessForConstituentTree(tree.toString());
//                System.out.println("\ntree = \n" + tree.toString());
//                System.out.println("preprocessedTree = \n" + preprocessedConstituentTree);
//
//                if (!checkParentheses(preprocessedConstituentTree)==true) {
//                    System.err.println("ERRPR!!!!!!!!!!!!!!!!!!!");
//                }
////                String preprocessedConstituentTree = tree.toString();
//                DTree dtree = convertConstituentToDependency(preprocessedConstituentTree);
//
//                System.out.println("dtree = \n" + dtree);
//
////                addPropToDTree(dtree, prop.row(counter));
//                try {
//                    fw.write(dtree.toString()); // TODO: This might be wrong because of the number of columns
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



//    public static void convertTreebankToCoNLLX(String trainDirPath, FileFilter trainTreeBankFilter, String outputFileName,
//                                               Table<Integer, Integer, Map<String, String>> prop) {
//        int counter = 0;
//        DiskTreebank trainTreeBank = new DiskTreebank();
//        trainTreeBank.loadPath(trainDirPath, trainTreeBankFilter);
//
//        try {
//            File theFile = new File(outputFileName);
//            if (!theFile.exists()) {
//                File theDirectory = new File(outputFileName.substring(0, outputFileName.lastIndexOf(File.separator)));
//                theDirectory.mkdirs();
//                theFile.createNewFile();
//            }
//
//            FileWriter fw = new FileWriter(theFile);
//
//            trainTreeBank.stream().forEachOrdered(tree -> {
//                count(counter, trainTreeBank);
//                // preprocee; constituent tree -> dependency tree
//                String preprocessedConstituentTree = preprocessForConstituentTree(tree.toString());
//                System.out.println("\ntree = \n" + tree.toString());
//                System.out.println("preprocessedTree = \n" + preprocessedConstituentTree);
//
//                if (!checkParentheses(preprocessedConstituentTree)==true) {
//                    System.err.println("ERRPR!!!!!!!!!!!!!!!!!!!");
//                }
////                String preprocessedConstituentTree = tree.toString();
//                DTree dtree = convertConstituentToDependency(preprocessedConstituentTree);
//
//                System.out.println("dtree = \n" + dtree);
//
////                addPropToDTree(dtree, prop.row(counter));
//                try {
//                    fw.write(dtree.toString()); // TODO: This might be wrong because of the number of columns
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//
//            fw.flush();
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /** ******************************************************
     * Collect all ontotrees from input file, convert them to
     * StandardTrees, and save to a new file
     */
    public static void convertTreebankToCoNLLX(String inputTreebankFile, String outputDependencyFile, Table<Integer, Integer, Map<String, String>> infoTable) {

        try {
            File theFile = new File(outputDependencyFile);
            if (!theFile.exists()) {
                File theDirectory = new File(outputDependencyFile.substring(0, outputDependencyFile.lastIndexOf(File.separator)));
                theDirectory.mkdirs();
                theFile.createNewFile();
            }

            FileWriter fw = new FileWriter(theFile);

            int counter = 0;
            BufferedReader reader = new BufferedReader(new FileReader(inputTreebankFile));
            StringBuilder stringbuilder = new StringBuilder();
            String stringseparator = System.getProperty("line.separator");
            String line = null;
            while ((line = reader.readLine()) != null) {
                // continue reading until an empty line is hit
                if (!line.isEmpty()) {
                    stringbuilder.append(line);
                    stringbuilder.append(stringseparator);
                } else {
                    String constituentTree = stringbuilder.toString().trim();
                    String preprocessedConstituentTree = preprocessForConstituentTree(constituentTree);
                    System.out.println("\nfilepath = " + inputTreebankFile + " \t counter = " + counter);
                    System.out.println(preprocessedConstituentTree);
                    if (!preprocessedConstituentTree.isEmpty() && checkParentheses(preprocessedConstituentTree)==true) {
                        DTree dtree = convertConstituentToDependency(preprocessedConstituentTree);
//                        System.out.println("\nconstituentTree = \n" + constituentTree);
//                        System.out.println("preprocessedTree = \n" + preprocessedConstituentTree);
//                        System.out.println("dtree = \n" + dtree);
//                        System.out.println("counter = " + counter);
//                        System.out.println("info = \n" + infoTable.row(counter));

//                        final int finalCounter = counter;
//                        final DTree finaldtree = dtree;
//                        infoTable.row(counter).entrySet().stream().forEach(e -> {
//                            int nid = e.getKey();
//                            String srlString = e.getValue().get("srl");
//                            String rel = srlString.substring(0, srlString.indexOf(":0"));
//                            String formInTable = e.getValue().get("form").split("-")[0];
//                            String formInDtree = finaldtree.get(nid+1).getLemma();
//                            if (!formInTable.equals(formInDtree)/**!e.getKey().toString().equals(rel)**/) {
//                                System.out.println("\nfilepath = " + inputTreebankDirectory + " \t counter = " + finalCounter);
//                                System.out.println("srlString = " + srlString);
//                                System.out.println("formInTable = " + formInTable);
//                                System.out.println("formInDtree = " + formInDtree);
//                            }
//
//                        });


                        List<DNode> list = dtree.stream().filter(x->x.getId()==0).collect(Collectors.toList());
                        if (list.isEmpty()) {
                            System.out.println("\nfilepath = " + inputTreebankFile + " \t counter = " + counter);
                        }

                        try {
                            fw.write(dtree.toString());
                            fw.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    }
//                    if (checkParentheses(preprocessedConstituentTree) == false) {
//                        System.out.println(index);
//                        System.out.println("-------ontotree-------\n" + constituentTree);
//                        System.out.println("-------standardtree-------\n" + preprocessedConstituentTree);
//                    }

                    // reconstruct string builder
                    stringbuilder = new StringBuilder();
                    counter++;
                }
            }
            String constituentTree = stringbuilder.toString().trim();
            if (!constituentTree.isEmpty()) {
                String preprocessedConstituentTree = preprocessForConstituentTree(constituentTree);
                if (!preprocessedConstituentTree.isEmpty() && checkParentheses(preprocessedConstituentTree)==true) {
                    DTree dtree = convertConstituentToDependency(preprocessedConstituentTree);
                    System.out.println("\nconstituentTree = \n" + constituentTree);
                    System.out.println("preprocessedTree = \n" + preprocessedConstituentTree);
                    System.out.println("dtree = \n" + dtree);
                    System.out.println("info = \n" + infoTable.row(counter));
                }
            }



            reader.close();
            fw.flush();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * run convertTreebankToCoNLLX for each file in the given directory
     * @param inputTreebankDirectory
     * @param inputPropDirectory
     * @param outputDirectory
     * @param filter
     */
    public static void forEachFile(String inputTreebankDirectory, String inputPropDirectory, String outputDependencyDirectory, FileFilter filter) {
        File theDir = new File(inputTreebankDirectory);
        File[] fList = theDir.listFiles();
        for (File file : fList) {
            String absolutePath = file.getAbsolutePath();
            if (file.isFile()) {
                String inputTreebankFilePath = file.getPath();
                String inputPropFilePath = inputPropDirectory
                        + inputTreebankFilePath.substring(inputTreebankFilePath.indexOf(TAG) + TAG.length(), inputTreebankFilePath.lastIndexOf(File.separator))
                        + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."))  + ".prop";
                String outputFilePath = outputDependencyDirectory + inputTreebankFilePath.substring(inputTreebankFilePath.indexOf(TAG) + TAG.length());


                if (file.getName().endsWith(".parse")) {
                    System.out.println("inputTreebankFilePath = " + inputTreebankFilePath);
                    System.out.println("inputPropFilePath = " + inputPropFilePath);
                    System.out.println("outputFilePath = " + outputFilePath);
                    System.out.println();
                    Table<Integer, Integer, Map<String, String>> info = readPropFile(inputPropFilePath);
                    convertTreebankToCoNLLX(inputTreebankFilePath, outputFilePath, info);
                }

            } else if (file.isDirectory()) {
                forEachFile(absolutePath, inputPropDirectory, outputDependencyDirectory, filter);
            }
        }
    }

    /**
     * Read props and SRL labels from prop file
     * @param inputPropFilePath
     * @return
     */
    private static Table<Integer, Integer, Map<String, String>> readPropFile(String inputPropFilePath) {
        Table<Integer, Integer, Map<String, String>> infoTable = HashBasedTable.create();
        try {
            Files.lines(Paths.get(inputPropFilePath)).forEach(l -> {
                String[] cols = l.split(" ");
                Integer treeID = Integer.parseInt(cols[1]);         // Indexing from "0"
                Integer nodeID = Integer.parseInt(cols[2]);         // Indexing from "0"
                String formString = cols[4];
                String propString = cols[5];
                String srlString = l.substring(l.indexOf(splitString) + splitString.length()).replaceAll("\\s", "|");
                Map<String, String> info = new HashMap<>();
                info.put("form", formString);
                info.put("prop", propString);
                info.put("srl", srlString);
                infoTable.put(treeID, nodeID, info);
            });
//            System.out.println("\n-----------------------------------" );
//            infoTable.cellSet().stream().forEach(e -> {
//                System.out.println(e.getRowKey() + "\t" + e.getColumnKey() + "\t" + e.getValue());
//            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoTable;
    }

    //    private static void addPropToDTree(DTree dtree, Map<Integer, Map<String, String>> prop) {
//        prop.entrySet().stream().forEach(e -> {
//            dtree.get(e.getKey()).addFeature(e.getValue().get("???????"));
//            dtree.get(e.getKey()).addSemanticHead("??????");
//
//        });
//    }

    public static void main(String[] args) {
        String inputConstituentDirectory = "data/ontonotes/treeBank";
        String inputPropDirectory = "data/ontonotes/treeBank";
        String outputDependencyDirectory = "data/ontonotes/dependencyTree";
        FileFilter filter = new ExtensionFileFilter(".parse", true);
        forEachFile(inputConstituentDirectory, inputPropDirectory, outputDependencyDirectory, filter);
    }
}
