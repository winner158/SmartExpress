package com.util;

import com.config.Config;

/**
 * 快递费用计算公式
 *
 * @Author: pfsun
 * @Date: 2020-01-02 17:22
 */
public class CalculateDeleiverExpense {


    /**快递费用计算公式
     * @param firstPrice
     * @param continuePrice
     * @param scale
     * @param weight
     * @param lengthP
     * @param widthP
     * @param heightP
     * @return
     */
    public static double calculate(double firstPrice, double continuePrice, int scale, double weight, double lengthP, double widthP, double heightP) {

        weight = weight>lengthP*widthP*heightP/scale?weight:lengthP*widthP*heightP/scale;

        if (weight > Config.firstWeightCriterion) {
            //已达到首重已达到首重
            double units = Math.floor((weight - Config.firstWeightCriterion) / Config.continueWeightCriterion) + ((weight - Config.firstWeightCriterion) % Config.continueWeightCriterion > 0 ? 1 : 0);
            return firstPrice + units * continuePrice;

        } else {
            //未达到首重
            return firstPrice;
        }
    }

    public static void main(String[] args) {
        System.out.println(calculate(6, 1, 6000, 1.9, 1, 1, 1));
    }
}
