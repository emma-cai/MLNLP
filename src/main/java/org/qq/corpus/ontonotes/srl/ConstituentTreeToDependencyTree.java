package org.qq.corpus.ontonotes.srl;

import edu.stanford.nlp.io.ExtensionFileFilter;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.*;
import org.qq.tools.stanfordparser.LangTools;
import org.qq.tools.stanfordparser.StanfordNNDepParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingqingcai on 7/20/15.
 */
public class ConstituentTreeToDependencyTree {

    public static void convertTreebankToCoNLLX(String trainDirPath, FileFilter trainTreeBankFilter, boolean makeCopulaVerbHead, String outputFileName) {
        int counter = 0;
        DiskTreebank trainTreeBank = new DiskTreebank();
        trainTreeBank.loadPath(trainDirPath, trainTreeBankFilter);

        SemanticHeadFinder headFinder = new SemanticHeadFinder(!makeCopulaVerbHead); // keep copula verbs as head

        try {
            File theFile = new File(outputFileName);
            if (!theFile.exists()) {
                File theDirectory = new File(outputFileName.substring(0, outputFileName.lastIndexOf(File.separator)));
                theDirectory.mkdirs();
                theFile.createNewFile();
            }

            FileWriter fw = new FileWriter(theFile);

            trainTreeBank.stream().forEachOrdered(tree -> {
                count(counter, trainTreeBank);
                Collection<TypedDependency> tdep = new EnglishGrammaticalStructure(tree, string -> true, headFinder, true).typedDependencies();
                String conllxString = getCoNLLXString(tdep, tree.taggedLabeledYield());
                try {
                    fw.write(conllxString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
     * @param inputDirectory
     * @param outputDirectory
     * @param filter
     */
    public static void forEachFile(String inputDirectory, String outputDirectory, FileFilter filter) {
        File theDir = new File(inputDirectory);
        File[] fList = theDir.listFiles();
        for (File file : fList) {
            String absolutePath = file.getAbsolutePath();
            if (file.isFile()) {
                String inputFilePath = file.getPath();
                String outputFilePath = outputDirectory + inputFilePath.substring(inputFilePath.indexOf("constituentTree") + "constituentTree".length());

                if (file.getName().endsWith(".parse")) {
                    System.out.println("path = " + inputFilePath);
                    System.out.println("outputpath = " + outputFilePath);
                    System.out.println();
                    convertTreebankToCoNLLX(inputFilePath, filter, true, outputFilePath);
                }
            } else if (file.isDirectory()) {
                forEachFile(absolutePath, outputDirectory, filter);
            }
        }
    }

    public static void main(String[] args) {
        String inputDirectory = "data/ontonotes/constituentTree";
        String outputFileName = "data/ontonotes/dependencyTree";
        FileFilter filter = new ExtensionFileFilter(".parse", true);
//        convertTreebankToCoNLLX(inputDirectory, filter, true, outputFileName);
        forEachFile(inputDirectory, outputFileName, filter);
    }
}
