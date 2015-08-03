package org.mlnlp.nlp.srl;

import edu.emory.clir.clearnlp.bin.NLPTrain;

/**
 * Created by qingqingcai on 8/3/15.
 */
public class SRLTrain {

    public static void main(String[] args) {
        NLPTrain.main(new String[] {
            "-mode", "dep",
            "-c", "/Users/qingqingcai/Documents/IntellijWorkspace/MLNLP/src/main/resources/configure/config_train_dep.xml",
            "-f", "/Users/qingqingcai/Documents/IntellijWorkspace/MLNLP/src/main/resources/features/feature_en_dep.xml",
            "-t", "/Users/qingqingcai/Desktop/train",
            "-d", "/Users/qingqingcai/Desktop/development",
            "-te", "*",
            "-de", "*"
        });
    }
}
