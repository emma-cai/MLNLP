package org.qq.corpus.ontonotes.srl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.stanford.nlp.io.ExtensionFileFilter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.trees.TypedDependency;
import org.qq.tools.stanfordparser.LangTools;
import org.qq.tools.stanfordparser.StanfordNNDepParser;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by qingqingcai on 7/20/15.
 */
public class ConstituentTreeToDependencyTree {

    private static final String splitString = " ----- ";

//    public static void convertTreebankToCoNLLX(String trainDirPath, FileFilter trainTreeBankFilter,
//                                               boolean makeCopulaVerbHead, String outputFileName,
//                                               Table<Integer, Integer, Map<String, String>> prop) {
//        int counter = 0;
//        DiskTreebank trainTreeBank = new DiskTreebank();
//        trainTreeBank.loadPath(trainDirPath, trainTreeBankFilter);
//
//        SemanticHeadFinder headFinder = new SemanticHeadFinder(!makeCopulaVerbHead); // keep copula verbs as head
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
//                Collection<TypedDependency> tdep = new EnglishGrammaticalStructure(tree, string -> true, headFinder, true).typedDependencies();
//                String conllxString = getCoNLLXString(tdep, tree.taggedLabeledYield());
//                DTree dtree = buildDTreeFromConllx(conllxString);
//                addPropToDTree(dtree, prop.row(counter));
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
//
//    private static void addPropToDTree(DTree dtree, Map<Integer, Map<String, String>> prop) {
//        prop.entrySet().stream().forEach(e -> {
//            dtree.get(e.getKey()).addFeature(e.getValue().get("???????"));
//            dtree.get(e.getKey()).addSemanticHead("??????");
//
//        });
//    }

    private static void count(int counter, Treebank trainTreeBank) {
        counter++;
        if (counter % 1000 == 0) {
            System.out.println("Processing " + counter + " of " + trainTreeBank.size());
        }
    }

    public static String getCoNLLXString(Collection<TypedDependency> deps, List<CoreLabel> tokens) {
        StringBuilder bf = new StringBuilder();

        Map<Integer, TypedDependency> indexedDeps = new HashMap<>(deps.size());
        for (TypedDependency dep : deps) {
            indexedDeps.put(dep.dep().index(), dep);
        }

        int idx = 1;

        if (tokens.get(0).lemma() == null) {
            StanfordNNDepParser.tagLemma(tokens);
        }

        for (CoreLabel token : tokens) {
            String word = token.word();
            String pos = token.tag();
            String cPos = (token.get(CoreAnnotations.CoarseTagAnnotation.class) != null) ?
                    token.get(CoreAnnotations.CoarseTagAnnotation.class) : LangTools.getCPOSTag(pos);
            String lemma = token.lemma();
            Integer gov = indexedDeps.containsKey(idx) ? indexedDeps.get(idx).gov().index() : 0;
            String reln = indexedDeps.containsKey(idx) ? indexedDeps.get(idx).reln().toString() : "erased";
            String out = String.format("%d\t%s\t%s\t%s\t%s\t_\t%d\t%s\t_\t_\n", idx, word, lemma, cPos, pos, gov, reln);
            bf.append(out);
            idx++;
        }
        bf.append("\n");
        return bf.toString();
    }

    /**
     * run convertTreebankToCoNLLX for each file in the given directory
     * @param inputTreebankDirectory
     * @param inputPropDirectory
     * @param outputDirectory
     * @param filter
     */
    public static void forEachFile(String inputTreebankDirectory, String inputPropDirectory, String outputDirectory, FileFilter filter) {
        File theDir = new File(inputTreebankDirectory);
        File[] fList = theDir.listFiles();
        for (File file : fList) {
            String absolutePath = file.getAbsolutePath();
            if (file.isFile()) {
                String inputTreebankFilePath = file.getPath();
                String inputPropFilePath = inputPropDirectory
                        + inputTreebankFilePath.substring(inputTreebankFilePath.indexOf("constituentTree") + "constituentTree".length(), inputTreebankFilePath.lastIndexOf(File.separator))
                        + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."))  + ".prop";
                String outputFilePath = outputDirectory + inputTreebankFilePath.substring(inputTreebankFilePath.indexOf("constituentTree") + "constituentTree".length());

                if (file.getName().endsWith(".parse")) {
                    System.out.println("inputTreebankFilePath = " + inputTreebankFilePath);
                    System.out.println("inputPropFilePath = " + inputPropFilePath);
                    System.out.println("outputFilePath = " + outputFilePath);
                    System.out.println();
//                    readTrees(inputConllxFilePath);
//                    convertTreebankToCoNLLX(input);
                    Table<Integer, Integer, Map<String, String>> info = readPropFile(inputPropFilePath);
//                    convertTreebankToCoNLLX(inputTreebankFilePath, filter, true, outputFilePath, prop);
                }

//                String inputFilePath = file.getPath();
//                String outputFilePath = outputDirectory + inputFilePath.substring(inputFilePath.indexOf("constituentTree") + "constituentTree".length());
//
//                if (file.getName().endsWith(".parse")) {
//                    System.out.println("path = " + inputFilePath);
//                    System.out.println("outputpath = " + outputFilePath);
//                    System.out.println();
//                    convertTreebankToCoNLLX(inputFilePath, filter, true, outputFilePath);
//                }
            } else if (file.isDirectory()) {
                forEachFile(absolutePath, inputPropDirectory, outputDirectory, filter);
            }
        }
    }

    private static Table<Integer, Integer, Map<String, String>> readPropFile(String inputPropFilePath) {
        Table<Integer, Integer, Map<String, String>> infoTable = HashBasedTable.create();
        try {
            Files.lines(Paths.get(inputPropFilePath)).forEach(l -> {
                String[] cols = l.split(" ");
                Integer treeID = Integer.parseInt(cols[1]);
                Integer nodeID = Integer.parseInt(cols[2]);
                String propString = cols[5];
                String srlString = l.substring(l.indexOf(splitString) + splitString.length()).replaceAll("\\s", "|");
                Map<String, String> info = new HashMap<>();
                info.put("prop", propString);
                info.put("srl", srlString);
                infoTable.put(treeID, nodeID, info);


            });
            System.out.println("\n-----------------------------------" );
            infoTable.cellSet().stream().forEach(e -> {
                System.out.println(e.getRowKey() + "\t" + e.getColumnKey() + "\t" + e.getValue());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String inputConllxDirectory = "data/ontonotes/constituentTree";
        String inputPropDirectory = "/Users/qingqingcai/Documents/ontonotes-release-5.0/data/files/data";
        String outputDirectory = "data/ontonotes/dependencySRLTree";
        FileFilter filter = new ExtensionFileFilter(".parse", true);
        forEachFile(inputConllxDirectory, inputPropDirectory, outputDirectory, filter);

//        convertTreebankToCoNLLX(inputDirectory, filter, true, outputFileName);
//        forEachFile(inputDirectory, outputFileName, filter);
    }
}
