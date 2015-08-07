package org.mlnlp.nlp.answerextraction.caching;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Takes high-ascii or non-ascii characters which cannot be parsed, and replaces them with low-ascii equivalents.
 * Works with two Map<String, String> objects--one for normal characters, and one for special regex characters such as
 * asterisk. Users should modify these two maps.
 * For improved runtime performance, this class converts these maps to Map<Pattern, String> objects.
 */
public class AsciiMapper {

    /**
     * A compiled Pattern object that matches all non-ascii characters.
     */
    public static final Pattern nonAsciiPattern = Pattern.compile("[^\\x00-\\x7F]");

    // These non-ascii characters should be replaced with the associated strings.
    public static ImmutableMap<String, String>
        normalCharEquivalenceMap = ImmutableMap.<String, String>builder()
            .put("`", "'")          // grave accent or "backwards apostrophe"
            .put("\\u00A0", " ")    // non breaking space
            .put("\\u2013", "-")    // en dash
            .put("\\u2014", "--")   // em dash
            .put("\\u2018", "'")    // left single quote
            .put("\\u2019", "'")    // right single quote
            .put("\\u00B9", "'")    // sometimes used as a right single quote
            .put("\\u201C", "\"")   // left double quote
            .put("\\u201D", "\"")   // right double quote
            .build();
    // Convert the map above into a map of compiled patterns.
    private static Map<Pattern, String> normalCharPatternMap = Maps.newHashMap();
    static  {
        for (Map.Entry<String, String> pair : normalCharEquivalenceMap.entrySet())   {
            Pattern pattern = Pattern.compile(pair.getKey());
            normalCharPatternMap.put(pattern, pair.getValue());
        }
    }

    // These special characters should be replaced with the associated strings.
    public static ImmutableMap<String, String>
        specialCharEquivalenceMap = ImmutableMap.<String, String>builder()
            .put("*", "")    // asterisk
            .build();
    // Convert the map above into a map of compiled patterns.
    private static Map<Pattern, String> specialCharPatternMap = Maps.newHashMap();
    static  {
        for (Map.Entry<String, String> pair : specialCharEquivalenceMap.entrySet())   {
            // Note the use of the escape characters, necessary because we're dealing with special characters.
            Pattern pattern = Pattern.compile("\\" + pair.getKey());
            specialCharPatternMap.put(pattern, pair.getValue());
        }
    }

    /**
     * Replace any character in the input with its equivalent according to the equivalenceMap.
     * @param input
     * @return
     */
    public static String replace(final String input)    {
        String retString = input;

        for (Pattern pattern : normalCharPatternMap.keySet())   {
            retString = pattern.matcher(retString).replaceAll(normalCharPatternMap.get((pattern)));
        }

        for (Pattern pattern : specialCharPatternMap.keySet())   {
            retString = pattern.matcher(retString).replaceAll(specialCharPatternMap.get((pattern)));
        }

        return retString;
    }

    /**
     * Replace any non-ascii character in the given string with the given replacement.
     * @param input
     * @param replacement
     * @return
     */
    public static String replaceNonAscii(final String input, final String replacement)  {
        String retString = input;

        retString = nonAsciiPattern.matcher(retString).replaceAll(replacement);

        return retString;
    }

    public static boolean containsNonAscii(String input) {
        return nonAsciiPattern.matcher(input).find();
    }
}
