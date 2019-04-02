package com.yoriz.yorizframework;

import java.util.Arrays;

/**
 * Created by yoriz
 * on 2019/3/25 5:07 PM.
 */
public class Test {
    public static void main(String[] args) {
        int[] a = {5, 2, 7, 1, 23, 15, 9, 34, 6, 8, 13};
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (j+1>a.length-1)continue;
                if (a[j]>a[j+1]){
                    int temp = a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
        }
        System.out.print(Arrays.toString(a));
    }
}
