package com;

import java.util.Random;

/**
 * @Author: pfsun
 * @Date: 2020-02-26 20:23
 */
public class Test {


    public static void main(String[] args) {
        double threshold = 0.3;
        double jinsibi_upper = (Math.log(20)+1)/(1-threshold);
        double jinsibi_low = (Math.log(20)+1)/(1-threshold)-2;
        Random random = new Random();
        double cpmparedValue = 5.813784;

        double re_upper = cpmparedValue/jinsibi_upper;
        double low_upper = cpmparedValue/jinsibi_low;
        System.out.println((re_upper+low_upper)/2);
    }

}

