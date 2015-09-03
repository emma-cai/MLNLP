package org.mlnlp.memnn.data;

import java.util.Arrays;

/**
 * Created by Maochen on 9/3/15.
 */
public class MemnetTuple {
    public int id = -1;

    public String sentence = null;

    public String shortAnswer = null;

    public int[] refId = null;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\t");
        sb.append(sentence).append("\t");
        if (shortAnswer != null) {
            sb.append(shortAnswer).append("\t");
        } else {
            sb.append("_").append("\t");
        }

        if (refId != null) {
            sb.append(Arrays.toString(refId)).append("\t");
        } else {
            sb.append("_").append("\t");
        }
        return sb.toString().trim();
    }
}
