package org.mlnlp.corpus.ontonotes.srl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.*;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qingqingcai on 7/21/15.
 */
public class PennPreprocessor {

    /**
     * Convert preprocessed constituent tree to dependency tree
     * @param constituentTree
     * @return
     */
    public static DTree convertConstituentToDependency(String constituentTree) {
        Tree tree = Tree.valueOf(constituentTree);
        SemanticHeadFinder headFinder = new SemanticHeadFinder(false);
        Collection<TypedDependency> dependencies = new EnglishGrammaticalStructure(tree, string->true, headFinder).typedDependencies();
        List<CoreLabel> tokens = tree.taggedLabeledYield();
        DTree dtree = StanfordTreeBuilder.generate(tokens, dependencies, null);
        return dtree;
    }

    public static void count(int counter, Treebank trainTreeBank) {
        counter++;
        if (counter % 1000 == 0) {
            System.out.println("Processing " + counter + " of " + trainTreeBank.size());
        }
    }

    /** ******************************************************
     * Convert ontotree to standard tree: (1) remove "(TOP ";
     * (2) remove "-***" in "NP-***" tags; (3) replace "-NONE-"
     * with "NN" if the tag of "-NONE-" is *noun-phrase*;
     * otherwise, replace "-NONE-"; (4) remove the last ")";
     * with "X";
     */
    public static String preprocessForConstituentTree(String ontotree) {

        String standardtree = "";
        // (1) remove the beginning tag "(TOP"
        standardtree = ontotree.replaceAll("^\\(TOP ", "");

        Pattern p = Pattern.compile("\\(XX ");
        Matcher m = p.matcher(standardtree);
        if (m.find()) return "";

        // remove "-***" in "NP-***" tags
        standardtree = standardtree.replaceAll("\\(((NP )|(NP-[A-Z]*[-]{0,1}[1-9]* ))\\(-NONE- ", "\\(NP \\(NN ");

        //
        standardtree = standardtree.replaceAll("\\(-NONE- 0\\)", "");
        // remove "-***" in tags

        standardtree = standardtree.replaceAll("-(([A-Z]*)|([1-9]*)|([A-Z]*-[1-9]*))[^ ] \\(", " \\(");

        // (4) remove the ending tag ")", which is balanced with "(TOP"
        standardtree = standardtree.replaceAll("\\)$", "");
        return standardtree;
    }

    /**
     * Check if the parentheses is legal or not?
     */
    public static boolean checkParentheses(String tree) {

        Stack<Character> stack = new Stack<Character>();
        char ch;
        for (int i = 0; i < tree.length(); i++) {
            ch = tree.charAt(i);
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.empty())
                    return false;
                else if (stack.peek() == '(')
                    stack.pop();
                else
                    return false;
            }
        }
        return stack.empty();
    }
}
