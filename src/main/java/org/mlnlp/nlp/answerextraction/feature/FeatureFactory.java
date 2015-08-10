package org.mlnlp.nlp.answerextraction.feature;

import org.maochen.nlp.datastructure.DNode;
import org.maochen.nlp.datastructure.DTree;
import org.maochen.nlp.datastructure.LangLib;
import org.maochen.nlp.ml.datastructure.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by qingqingcai on 8/7/15.
 */
public class FeatureFactory {

    public final static String SAE_LABEL = "SAE";
    public final static String NON_SAE_LABEL = "NON_SAE";

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFactory.class);

    /**
     *
     */
    public List<Tuple> extractFeatures(DTree queDTree, DTree senDTree, List<String> labels) {

        List<Tuple> tuples = new ArrayList<>();
        if (labels == null || senDTree.size() != labels.size()) {
            LOG.error("The size of labels does not match with the training data!");
        } else {
            tuples = senDTree.stream()
                .filter(n -> n.getId() != 0)
                .map(n -> generateTuple(queDTree, senDTree, n, labels.get(n.getId())))
                .collect(Collectors.toList());
        }

        return tuples;
    }

    /**
     *
     */
    public Tuple generateTuple(DTree queDTree, DTree senDTree, DNode candidate, String label) {
        List<String> feats = generateFeats(queDTree, senDTree, candidate);     // TODO

        // convert to Tuple feats
//        String[] featsName = feats.stream().toArray(String[]::new);
//        double[] featsVector = feats.stream().mapToDouble(x -> 1D).toArray();
        Map<String, Double> featsMap = new HashMap<>();
        feats.stream().filter(s -> !s.isEmpty())
            .forEach(s -> {
                String[] tokens = s.split(",");
                for (String token : tokens) {
                    String[] kv = token.split("=");
                    String key = kv[0].trim();
                    Double value = kv.length == 2 ? Double.parseDouble(kv[1].trim()) : 1D;
                    featsMap.put(key, value);
                }
            });
        String[] featsName = featsMap.keySet().toArray(new String[featsMap.keySet().size()]);
        double[] featsVector = featsMap.values().stream().mapToDouble(d -> d).toArray();

        // label
        label = label.isEmpty() ? NON_SAE_LABEL : label;
        if (!NON_SAE_LABEL.equals(label) && !SAE_LABEL.equals(label)) {
            LOG.error("label can be either " + SAE_LABEL + " or " + NON_SAE_LABEL);
        }

        return new Tuple(1, featsName, featsVector, label);
    }

    public List<String> generateFeats(DTree queDTree, DTree senDTree, DNode candidate) {
        List<String> feats = new ArrayList<>();

    //    feats.add(candidate.getLemma());

        // pos (categorical)
        feats.add(candidate.getPOS());

        // dependency label (categorical)
        feats.add(candidate.getDepLabel());

        // candidate's children contains POS="CD" or not (binary)
        List<DNode> children = candidate.getChildren();
        children.add(candidate);
        boolean flag = false;
        for (DNode n : children) {
            if (n.getPOS().startsWith(LangLib.POS_CD)) {
                flag = true;
                break;
            }
        }
        feats.add("bfsContainsCD=" + (flag ? "1.0" : "0.0"));

    //    feats.add("depth=" + Math.abs(candidate.getId() - senDTree.getRoots().get(0).getId()));

//        feats.add("pos_NN");
//        feats.add("pos_NNP");
//        feats.add("dep_nsubj");
//        feats.add("depth=2.0");
        return feats;
    }


}
