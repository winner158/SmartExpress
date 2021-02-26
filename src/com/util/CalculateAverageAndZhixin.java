package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**计算置信区间
 * @Author: pfsun
 * @Date: 2020-03-04 16:37
 */
public class CalculateAverageAndZhixin {

    public static double zhixinqujian(List<Double> list,double average){

        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            double x = list.get(i);
            total += Math.pow(x-average,2);
        }
        return Math.sqrt(total/list.size());
    }


    public static void moving_cost(String[] contents){
        System.out.println("平均移动成本");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*movingcost.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }

    public static void charge_cost(String[] contents){
        System.out.println("total charge");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*totalcharge.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }

    public static void total_cost(String[] contents){
        System.out.println("最优解");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*最优解.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }


    public static void running_time(String[] contents){
        System.out.println("程序的运行时间为");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*程序的运行时间为.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }


    public static void moving_cost_capp(String[] contents){
        System.out.println("移动成本");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*移动成本.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }

    public static void charge_cost_capp(String[] contents){
        System.out.println("total charge");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*平均快递费.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }

    public static void total_cost_capp(String[] contents){
        System.out.println("平均寄件成本");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*平均寄件成本.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split(":");
//                System.out.println(str);
                total += Double.parseDouble(split[1]);
                list.add(Double.parseDouble(split[1]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }


    public static void running_time_capp(String[] contents){
        System.out.println("程序运行时间");
        int count = 0;
        double total = 0;
        List<Double> list = new ArrayList<>();
        for (String str :
                contents){
            String pattern = ".*运行时间.*";
            if(Pattern.matches(pattern, str)){
                String[] split = str.split("：");
//                System.out.println(str);
                total += Double.parseDouble(split[2]);
                list.add(Double.parseDouble(split[2]));
            }
        }
        System.out.println("average is :"+total/list.size());
        System.out.println("zhixinqujian is :"+zhixinqujian(list,total/list.size()));
    }

    public static void main(String[] args) {
        String consoleContent = null;
        try {
            consoleContent = ImportAndExport.importData("/Users/pfsun/Desktop/实验第二部分/opt运行结果es6user9.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(consoleContent);
        //2.数据预处理
        String[] contents = consoleContent.split("-");
        moving_cost_capp(contents);
        charge_cost_capp(contents);
        total_cost_capp(contents);
//        running_time_capp(contents);

        moving_cost(contents);
        charge_cost(contents);
        total_cost(contents);
//        running_time(contents);

        //System.out.println((Math.log(5)+1)/(1-0.3));

    }

/**
 * opt user：5
 * 平均移动成本
 * average is :1.7521101528028096
 * zhixinqujian is :0.1094356165573806
 * total charge
 * average is :13.449999999999996
 * zhixinqujian is :0.21278575558006138
 * 最优解
 * average is :9.12126609168169
 * zhixinqujian is :0.1291208326771097
 * 程序的运行时间为
 * average is :141576.27
 * zhixinqujian is :14624.759753825021
 */

/**
 * capp user：5
 * 平均移动成本
 * average is :44.41200000000629
 * zhixinqujian is :6.288303211476887E-12
 * total charge
 * average is :9.6440944881892
 * zhixinqujian is :0.2690471794551522
 * 最优解
 * average is :54.05609448818643
 * zhixinqujian is :0.2690471794551494
 * 程序的运行时间为
 * average is :1.1163294972743785
 * zhixinqujian is :3.173252057922488
 */


}
