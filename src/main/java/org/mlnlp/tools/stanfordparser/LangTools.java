package org.mlnlp.tools.stanfordparser;

import org.maochen.nlp.datastructure.DNode;
import org.maochen.nlp.datastructure.LangLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingqingcai on 7/20/15.
 */
public class LangTools {

    /**
     * http://en.wikipedia.org/wiki/Wikipedia:List_of_English_contractions
     */
    private static final Map<String, String> contractions = new HashMap<String, String>() {{
        put("'m", "am");
        put("'re", "are");
        put("'ve", "have");
        put("can't", "cannot");
        put("ma'am", "madam");
        put("'ll", "will");
    }};

    public static void generateLemma(DNode node) {
        // Resolve 'd
        if (node.getForm().equalsIgnoreCase("'d") && node.getPOS().equals(LangLib.POS_MD)) {
            node.setLemma(node.getLemma());
        } else if (contractions.containsKey(node.getForm())) {
            node.setLemma(contractions.get(node.getForm()));
        }
    }

    public static String getCPOSTag(String pos) {
        if (pos.equals(LangLib.POS_NNP) || pos.equals(LangLib.POS_NNPS)) {
            return LangLib.CPOSTAG_PROPN;
        } else if (pos.equals(LangLib.POS_NN) || pos.equals(LangLib.POS_NNS)) {
            return LangLib.CPOSTAG_NOUN;
        } else if (pos.startsWith(LangLib.POS_VB)) {
            return LangLib.CPOSTAG_VERB;
        } else if (pos.startsWith(LangLib.POS_JJ)) {
            return LangLib.CPOSTAG_ADJ;
        } else if (pos.equals(LangLib.POS_IN) || pos.equals(LangLib.POS_TO)) {
            return LangLib.CPOSTAG_ADP;
        } else if (pos.startsWith(LangLib.POS_RB) || pos.equals(LangLib.POS_WRB)) {
            return LangLib.CPOSTAG_ADV;
        } else if (pos.equals(LangLib.POS_MD)) {
            return LangLib.CPOSTAG_AUX;
        } else if (pos.equals(LangLib.POS_CC)) {
            return LangLib.CPOSTAG_CONJ;
        } else if (pos.equals(LangLib.POS_CD)) {
            return LangLib.CPOSTAG_NUM;
        } else if (pos.equals(LangLib.POS_DT) || pos.equals(LangLib.POS_WDT) || pos.equals(LangLib.POS_PDT) || pos.equals(LangLib.POS_EX)) {
            return LangLib.CPOSTAG_DET;
        } else if (pos.equals(LangLib.POS_POS) || pos.equals(LangLib.POS_RP)) {
            return LangLib.CPOSTAG_PART;
        } else if (pos.startsWith(LangLib.POS_PRP) || pos.startsWith(LangLib.POS_WP)) {
            return LangLib.CPOSTAG_PRON;
        } else if (pos.equals(LangLib.POS_UH)) {
            return LangLib.CPOSTAG_INTJ;
        } else if (pos.equals(LangLib.POS_WRB)) {
            return LangLib.CPOSTAG_X;
        } else if (pos.equals(LangLib.POS_SYM) || pos.equals("$") || pos.equals("#")) {
            return LangLib.CPOSTAG_SYM;
        } else if (pos.equals(".") || pos.equals(",") || pos.equals(":") || pos.equals("``") || pos.equals("''") || pos.equals(LangLib.POS_HYPH) || pos.matches("-.*B-")) {
            return LangLib.CPOSTAG_PUNCT;
        } else { // FW
            return LangLib.CPOSTAG_X;
        }
    }
}
