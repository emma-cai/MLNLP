package org.mlnlp.nlp.answerextraction.evaluation;

import org.apache.commons.lang3.StringUtils;
import org.mlnlp.nlp.answerextraction.caching.AsciiMapper;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by qingqingcai on 8/10/15.
 */
public class EvaluationUtil {
    /**
     * Default evaluation function for short answer. Return false if the expected is not found in
     * the actual, or if the actual is "significantly" longer than the expected.
     */
    public static boolean evaluateForShortAnswer(final String expected, final String actual) {
        // Replace non-ascii chars that may be in the .json files.
        String expectedCleaned = AsciiMapper.replace(expected);

        Pattern expectedPattern = Pattern.compile(expectedCleaned.toLowerCase());

        // Split actual on short answer prefix, so that we are testing on just the short answer.
        // If the short answer prefix is not found, we expect to throw an index out of bounds exception.
        String[] actualArr = actual.split("TDODODODOODODOOD");
        String actualShortAnswer = actualArr.length == 1 ? actual : actualArr[1];

        actualShortAnswer = AsciiMapper.replace(actualShortAnswer);

        boolean found = expectedPattern.matcher(actualShortAnswer.toLowerCase()).find();

        // Get ratio of expected length to actual length.
        // Expected length is tricky if a regular expression, as in (hello|hi|what's up|how's it going|how have you been).
        // We'll take the length of the longest possibility--not perfect, but maybe good enough.
        String[] orSplit = expectedCleaned.trim().split("\\|");
        String longestStr = Arrays.asList(orSplit).stream()
            .max((s1, s2) -> s1.trim().split(StringUtils.SPACE).length - s2.trim().split
                (StringUtils.SPACE).length)
            .orElse(StringUtils.EMPTY);
        double expectedLength = longestStr.trim().split(StringUtils.SPACE).length;

        double actualLength = actualShortAnswer.trim().split(StringUtils.SPACE).length;
        double lengthRatio = actualLength / expectedLength;

        // Allowable length ratio depends on how long the expected length is. If expected is one word in length, we want to allow a two-word answer.
        double allowableLengthRatio = 2.0;
        if (expectedLength >= 3) {
            allowableLengthRatio = 1.4;
        }

        if (!found || lengthRatio > allowableLengthRatio) {
            return false;
        }
        return true;
    }

    public static boolean evaluateForLongAnswer(final String expected, final String actual, final String actualInOriginalSentence) {
        Pattern expectedPattern = Pattern.compile(expected.trim().toLowerCase());
        boolean found = expectedPattern.matcher(actual.trim().toLowerCase()).find();
        found |= expectedPattern.matcher(actualInOriginalSentence.toLowerCase()).find();
        return found;
    }
}
