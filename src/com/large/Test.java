package com.large;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: pfsun
 * @Date: 2020-03-05 17:46
 */
public class Test {

    public static void main(String args[]) {
        Integer[] a= {1,2,3,4,5,6,7,8,9,10,11,13,12,14,15,16,17,18,19,20,21,22,23,24,25,26,27};
        List<Integer> arr = Arrays.asList(a);

        ArrayList<ArrayList<Integer>> list=getSubArray(arr,arr.size());
        System.out.println(list.size());
        //输出列表：
        for (int i=0;i<list.size();i++) {
            ArrayList<Integer> mList=list.get(i);
            System.out.println(mList);
        }
    }

    private static ArrayList<ArrayList<Integer>> getSubArray(List<Integer> arr, int length) {
        ArrayList<ArrayList<Integer>> bList=new ArrayList<>();
        int mark=0;
        int nEnd=1<<length;
        boolean bNullset=false;
        for (mark=0;mark<nEnd;mark++) {
            ArrayList<Integer> aList=new ArrayList<>();
            bNullset=true;
            for (int i=0;i<length;i++) {
                if (((1<<i)&mark)!=0) {
                    bNullset=false;
                    aList.add(arr.get(i));
                }
            }
            bList.add(aList);
        }
        return bList;
    }

}
