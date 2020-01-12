package com.algorithm;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.google.common.collect.Lists;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;

import java.util.*;

/**
 * 数学优化的算法设计
 *
 * @Author: pfsun
 * @Date: 2020-01-08 16:56
 */
public class MathOptimizationAlgorithmDesign {


    //计算成本 实验要求：快递点的数目为3
    public static void calculateMinValue(List<ExpressS> ESlist, List<User> userList, List<Integer> numList) {
        List<Integer> prefix = new ArrayList<>();
        List<Object> fullUserList = new ArrayList<>();
//        List<Integer> numList = Lists.newArrayList(1, 2, 3, 4, 5);
        fullArray(numList, prefix, fullUserList);
        System.out.println(fullUserList);
        int group_size = ESlist.size();
        int balance = numList.size() % group_size > 0 ? 1 : 0;
        int set_size = numList.size() / group_size + balance;
        HashMap<Double, Object> hashMap = new HashMap<>();
        while (set_size <= numList.size()) {
            for (Object re :
                    fullUserList) {
                List<Integer> list = (List<Integer>) re;
                List<List<Integer>> lists = Lists.partition(list, set_size);
                System.out.println(lists.size());
                System.out.println(lists);
                if (lists.size() == group_size) {
                    //不需要分情况讨论
                    double cost = 0;
                    for (int i = 0; i < lists.size(); i++) {
                        List<Integer> userid_list = lists.get(i);
                        cost += calculateCost(ESlist, userList, userid_list, i);
                    }
                    hashMap.put(cost, lists);
                } else {
                    //1.补充 空集 进list
//                    List<Integer> zero = new ArrayList<>();
//                    while (lists.size() < group_size) {
//                        lists.add(zero);
//                    }
                    //2.子集全排列
                    List<Integer> sortList = Lists.newArrayList(0, 1, 2);
                    List<Object> outSortList = new ArrayList<>();
                    fullArray(sortList, new ArrayList<>(), outSortList);
                    for (int i = 0; i < outSortList.size(); i++) {
                        double cost = 0;
                        List<Integer> sortSetList = (List<Integer>) outSortList.get(i);
                        for (int j = 0; j < sortSetList.size(); j++) {
                            if(j<lists.size()){
                                List<Integer> userid_list = lists.get(j);
                                cost += calculateCost(ESlist, userList, userid_list, sortSetList.get(j));
                            }
                        }
                        hashMap.put(cost, lists);
                    }
                    //3.分配
                }

            }
            set_size++;
        }
        System.out.println(hashMap);
    }

    //计算总成本
    public static double calculateCost(List<ExpressS> ESlist, List<User> userList, List<Integer> userid_list, int es_id) {

        ExpressS express = ESlist.get(es_id);
        double totalWeight = 0;
        double totalCost = 0;
        for (Integer userid :
                userid_list) {
            User user = userList.get(userid);
            totalWeight += user.getWeight();
            //移动成本
            double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), express.getJingdu(), express.getWeidu());
            double movingCost = Config.unitCost * dist;
            totalCost += movingCost;
        }
        double deleiverExpenseTotal = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), totalWeight, 0, 0, 0);
        totalCost += deleiverExpenseTotal;
        return totalCost;
    }


    //全排列算法
    public static void fullArray(List<Integer> candidate, List<Integer> prefix, List<Object> result) {
        if (prefix.size() != 0 && candidate.size() == 0) {
            result.add(prefix);
        }
        for (int i = 0; i < candidate.size(); i++) {
            List<Integer> temp = new LinkedList<Integer>(candidate);
            int item = (int) temp.remove(i);  // 取出被删除的元素，这个元素当作一个组合用掉了
            List<Integer> temp_list = new ArrayList<>();
            temp_list.addAll(prefix);
            temp_list.add(item);
            fullArray(temp, temp_list, result);
        }
    }


    public static void main(String[] args) {

//        Integer[] array = {1,2,3};
//        List<Integer> list = Arrays.asList(array);
////        DFS(list, "");
        List<Integer> prefix = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        List<Integer> numList = Lists.newArrayList(1, 2, 3);
        fullArray(numList, prefix, result);
        System.out.println(result);
//        int partition = 3;
//        int balance = numList.size() % partition > 0 ? 1 : 0;
//        int set_size = numList.size() / partition + balance;
//        while (set_size <= numList.size()) {
//            for (Object re :
//                    result) {
//                List<Integer> list = (List<Integer>) re;
//                List<List<Integer>> lists = Lists.partition(list, set_size);
//            }
//            set_size++;
//        }

    }

}
