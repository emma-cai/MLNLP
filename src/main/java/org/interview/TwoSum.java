package org.interview;

/**
 * Created by qingqingcai on 8/28/15.
 */
public class TwoSum {

    public static int[] twoSum(int[] numbers, int target) {
        int[] result = new int[2];
        int length = numbers.length;
        for (int i = 0; i < length; i++) {
            int first = numbers[i];
            for (int j = i+1; j < length; j++) {
                int second = numbers[j];
                if (first + second == target) {
                    result[0] = i+1;
                    result[1] = j+1;
                    return result;
                }
            }
        }
        return null;
    }





    public static void main(String[] args) {
        int[] numbers = new int[]{3, 6, 1, 4, 9, 8, 10};
        int target = 16;
        int[] result = twoSum(numbers, target);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}
