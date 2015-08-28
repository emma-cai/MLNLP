package org.interview;

/**
 * Created by qingqingcai on 8/28/15.
 */
public class ReverseAString {

    /**
     * Write a function that reverses a String
     * @param inputStr the string
     * @return the reversed string
     * @throws InvalidInputStringException if the String is null or length < 2
     */
    public static String reverse(String inputStr) throws InvalidInputStringException {
        if (inputStr == null || inputStr.length() < 2) {
            throw new InvalidInputStringException();
        }
//        return tryStringBuffer(inputStr);
        return tryCharacterArray(inputStr);
    }

    private static String try

    private static String tryCharacterArray(String inputStr) {
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = inputStr.toCharArray();
        for (int i = chars.length-1; i >= 0; i--) {
            stringBuffer.append(chars[i]);
        }
        return stringBuffer.toString();
    }

    private static String tryStringBuffer(String inputStr) {
        return new StringBuilder(inputStr).reverse().toString();
    }

    public static void main(String[] args) {
        String inputStr = "abcdefg";
        try {
            String reversed = reverse(inputStr);
            System.out.println("inputStr = " + inputStr);
            System.out.println("reversed = " + reversed);
        } catch (InvalidInputStringException e) {
            e.printStackTrace();
        }
    }

    private static class InvalidInputStringException extends Exception {
    }
}
