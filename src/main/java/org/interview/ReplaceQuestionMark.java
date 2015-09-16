package org.interview;

import java.util.HashSet;
import java.util.Set;

/**
 * Interview coding question: replace question mark with J and K.
 *
 * Created by qingqingcai on 9/15/15.
 */
public class ReplaceQuestionMark {

    private static int num = 1;

    public static void main(String[] args) {
        String inputString = "?JK?JK??";
//        replaceQuestionMark1(inputString);
        replaceQuestionMark2(inputString);
    }

    /**
     * Example input: "JK?J?", print out "JKJJJ, JKJJK, JKKJJ, JKKJK".
     * This is a very naive and inefficient solution.
     */
    public static void replaceQuestionMark1(String inputString) {
        Set<String> output = new HashSet<>();
        output.add(inputString);
        for (int i = 0; i < inputString.length(); i++) {
            if (inputString.charAt(i) == '?') {
                Set<String> tmp = new HashSet<>(output);
                for (String o : output) {
                    tmp.remove(o);
                    tmp.add(o.substring(0, i) + "J" + o.substring(i+1, inputString.length()));
                    tmp.add(o.substring(0, i) + "K" + o.substring(i+1, inputString.length()));
                }
                output.clear();
                output.addAll(tmp);
            }
        }

        System.out.println("Total output: " + output.size());
        for (String o : output) {
            System.out.println(o);
        }
    }

    /**
     * Recursive solution.
     */
    public static void replaceQuestionMark2(String inputString) {
        replaceQUestionMark2Helper(inputString, 0, inputString.length()-1, "");
    }

    public static void replaceQUestionMark2Helper(String inputString,
                                                   int start, int end,
                                                   String processed) {
        if (inputString.length() == processed.length()) {
            System.out.println(num + ": " + processed);
            num++;
        }

        for (int i = start; i <= end; i++) {
            if (inputString.charAt(i) == '?') {
                replaceQUestionMark2Helper(inputString, i+1, end, processed+"J");
                replaceQUestionMark2Helper(inputString, i+1, end, processed+"K");
            } else {
                processed += inputString.charAt(i);
            }
        }
    }
}
