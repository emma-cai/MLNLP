package org.qq.corpus.ontonotes.srl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.SemanticHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import org.maochen.nlp.datastructure.DTree;
import org.qq.tools.stanfordparser.StanfordTreeBuilder;

import java.util.Collection;
import java.util.List;

import static org.qq.corpus.ontonotes.srl.ConstituentTreeToDependencyTreeUtils.preprocessForConstituentTree;

/**
 * Created by qingqingcai on 7/21/15.
 */
public class Experiment {

    public static void main(String[] args) {
        String constituentTree = "(TOP (S (NP-SBJ (NP (NP (NNP Moody)\n" +
                "                        (POS 's)))\n" +
                "                (NNP Investors)\n" +
                "                (NNP Service)\n" +
                "                (NNP Inc.))\n" +
                "        (VP (VBD said)\n" +
                "            (SBAR (-NONE- 0)\n" +
                "                  (S (NP-SBJ (PRP it))\n" +
                "                     (VP (VBD lowered)\n" +
                "                         (NP (NP (DT the)\n" +
                "                                 (NN debt)\n" +
                "                                 (NNS ratings))\n" +
                "                             (PP (IN of)\n" +
                "                                 (NP (NP (JJ certain)\n" +
                "                                         (NML (JJ long)\n" +
                "                                              (HYPH -)\n" +
                "                                              (NN term))\n" +
                "                                         (NN debt))\n" +
                "                                     (VP (VBN held)\n" +
                "                                         (NP (-NONE- *))\n" +
                "                                         (PP (IN by)\n" +
                "                                             (NP-LGS (DT this)\n" +
                "                                                     (NN company)))))))))))\n" +
                "        (. .)))";

        // preprocessing for constituentTree
        String processedConstituentTree = preprocessForConstituentTree(constituentTree);

        // converting to dependencyTree
        DTree dtree = convertConstituentToDependency(processedConstituentTree);
        System.out.println(dtree);
    }



    private static DTree convertConstituentToDependency(String constituentTree) {
        Tree tree = Tree.valueOf(constituentTree);
        SemanticHeadFinder headFinder = new SemanticHeadFinder(false);
        Collection<TypedDependency> dependencies = new EnglishGrammaticalStructure(tree, string->true, headFinder).typedDependencies();
        List<CoreLabel> tokens = tree.taggedLabeledYield();
        DTree dtree = StanfordTreeBuilder.generate(tokens, dependencies, null);
        return dtree;
    }

}
