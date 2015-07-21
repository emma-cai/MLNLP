package org.qq.corpus.ontonotes.srl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.qq.corpus.ontonotes.srl.ConstituentTreeToDependencyTreeUtils.preprocessForConstituentTree;

/**
 * Created by qingqingcai on 7/20/15.
 */
public class PreprocessForConstituentTreeTest {

    private void makeAssertion(String input, String expected) {
        String actual = preprocessForConstituentTree(input);
        assertEquals(expected, actual);
    }

    @Test
    public void test() {

        String input = "(TOP (FOO))";
        String expected = "(FOO)";
        makeAssertion(input, expected);

        input = "(TOP (TOP (FOO)))";
        expected = "(TOP (FOO))";
        makeAssertion(input, expected);

        input = "(TOP (SBAR (-NONE- *EXP*-2)))";
        expected = "(SBAR (X *EXP*-2))";
        makeAssertion(input, expected);

        input = "(TOP (NP (-NONE- *T*-1)))";
        expected = "(NP (NN *T*-1))";
        makeAssertion(input, expected);

        input = "(TOP (NP-SBJ (-NONE- *-2)))";
        expected = "(NP (NN *-2))";
        makeAssertion(input, expected);

        input = "(TOP (NP-SBJ-2 (-NONE- *T*-1)))";
        expected = "(NP (NN *T*-1))";
        makeAssertion(input, expected);

        input = "(TOP (S-ADV (NP (NN *PRO*-2))))";
        expected = "(S (NP (NN *PRO*-2)))";
        makeAssertion(input, expected);

        input = "(TOP (ADJP-PRD (-NONE- *?*)))";
        expected = "(ADJP (X *?*))";
        makeAssertion(input, expected);

        input = "(TOP (SINV (VP-TPC-1 (VBG Standing))))";
        expected = "(SINV (VP (VBG Standing)))";
        makeAssertion(input, expected);

        input = "(TOP (XX As)\n" +
                "     (XX the))";
        expected = "";
        makeAssertion(input, expected);

        input = "(TOP (NP (S (NP-SBJ (-NONE- *PRO*-1)))))";
        expected = "(NP (S (NP (NN *PRO*-1))))";
        makeAssertion(input, expected);

        input = "(TOP (S (NP-SBJ-2 (NP (-NONE- *T*-1)))))";
        expected = "(S (NP (NP (NN *T*-1))))";
        makeAssertion(input, expected);

//        input = "(TOP (S (CC But)\n" +
//                "        (NP-SBJ (PRP it))\n" +
//                "        (VP (VBD did)\n" +
//                "            (VP (VB provide)\n" +
//                "                (NP (PRP us))\n" +
//                "                (PP-CLR (IN with)\n" +
//                "                        (NP (NP (DT a)\n" +
//                "                                (VBN written)\n" +
//                "                                (NN statement))\n" +
//                "                            (VP (VBG saying)\n" +
//                "                                (S-SEZ (INTJ (UH quote))\n" +
//                "                                       (PP-MNR (IN like)\n" +
//                "                                               (NP (DT any)\n" +
//                "                                                   (NN company)))\n" +
//                "                                       (NP-SBJ-1 (PRP we))\n" +
//                "                                       (VP (VBP want)\n" +
//                "                                           (S (NP-SBJ (-NONE- *PRO*-1))\n" +
//                "                                              (VP (TO to)\n" +
//                "                                                  (VP (VB make)\n" +
//                "                                                      (S (NP-SBJ (NP (-NONE- *PRO*))\n" +
//                "                                                                 (SBAR (-NONE- *EXP*-2)))\n" +
//                "                                                         (ADJP-PRD (JJ sure))\n" +
//                "                                                         (SBAR-2 (-NONE- 0)\n" +
//                "                                                                 (S (NP-SBJ (PRP$ our)\n" +
//                "                                                                            (NML (NML (NNS associates))\n" +
//                "                                                                                 (NML (NNS customers))\n" +
//                "                                                                                 (CC and)\n" +
//                "                                                                                 (NML (JJ local)\n" +
//                "                                                                                      (NN community))))\n" +
//                "                                                                    (VP (VBP feel)\n" +
//                "                                                                        (ADJP-PRD (JJ good)\n" +
//                "                                                                                  (PP (IN about)\n" +
//                "                                                                                      (NP (PRP us))))))))))))))))))\n" +
//                "        (. /.)))";
//        expected = "???";
//        makeAssertion(input, expected);

    }

    @Test
    public void test1() {
        String input = "(VP (VBD said)\n" +
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
                "                                                     (NN company)))))))))))";
        String expected = "(VP (VBD said)\n" +
                "            (SBAR (S (NP-SBJ (PRP it))\n" +
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
                "                                                     (NN company)))))))))))";
        makeAssertion(input, expected);
    }
}
