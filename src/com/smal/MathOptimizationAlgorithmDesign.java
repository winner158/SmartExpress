package com.smal;

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
    public static void calculateMinValue(List<ExpressS> ESlist, List<User> userList) {

        List<Integer> numList = new ArrayList<>();
        for (User user:userList) {
            numList.add(user.getId());
        }
        System.out.println(numList);

        System.out.println("1");
        List<Integer> prefix = new ArrayList<>();
        List<Object> fullUserList = new ArrayList<>();
        //List<Integer> numList = Lists.newArrayList(1, 2, 3, 4, 5);
        fullArray(numList, prefix, fullUserList);
        System.out.println(fullUserList);
        int group_size = ESlist.size();
        int balance = numList.size() % group_size > 0 ? 1 : 0;
        int set_size = numList.size() / group_size + balance;

        HashMap<Integer, Object> userGroup = new HashMap<Integer, Object>();
        HashMap<Integer, Double> hashMap = new HashMap<>();
        while (set_size <= numList.size()) {
            int index = 0;
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
                    //hashMap.put(lists, cost);
                } else {

                    //1.补充 空集 进list
//                    List<Integer> zero = new ArrayList<>();
//                    while (lists.size() < group_size) {
//                        lists.add(zero);
//                    }
                    //2.子集全排列

                    double cost = 0;
                    for (int i = 0; i < lists.size(); i++) {

                        List<Integer> sortList = lists.get(i);
//                        List<Object> outSortList = new ArrayList<>();
//                        fullArray(sortList, new ArrayList<>(), outSortList);
//                        List<Integer> sortSetList = (List<Integer>) outSortList.get(i);
                        for (int j = 0; j < sortList.size(); j++) {
                            if(j<lists.size()){
                                List<Integer> userid_list = lists.get(j);
                                cost += calculateCost(ESlist, userList, userid_list, ESlist.get(j).getId());
                            }
                        }

                    }
                    System.out.println("cost:"+cost+" lists:"+lists);
                    hashMap.put(index, cost);
                    userGroup.put(index,lists);
                    index++;
                }
            }
            //3.分配

            set_size++;
        }
        int minListsId = (Integer) getKey(hashMap, (double) gerMinValue(hashMap));
        List<List<Integer>> optimalUserSet = (List<List<Integer>>) userGroup.get(minListsId);

        HashMap<Integer, Object> userSelectMap = new LinkedHashMap<>();
        for (ExpressS e :
                ESlist) {
            userSelectMap.put(e.getId(), new ArrayList<Integer>());
        }

        for (int i = 0; i < optimalUserSet.size(); i++) {

            List<Integer> userIdList = optimalUserSet.get(i);
            userSelectMap.put(i,userIdList);

        }
        calcluateResult(userSelectMap,ESlist,userList);
    }

    //计算平均移动成本、平均快递费、平均寄件成本
    public static void calcluateResult(HashMap<Integer, Object> userSelectMap, List<ExpressS> ESlist, List<User> userList){

        double totalMovingCost = 0;
        double totalExpressExpense = 0;
        double totalCharge = 0;

        for (ExpressS e :
                ESlist) {
            List<Integer> userSetbyEs = (List<Integer>) userSelectMap.get(e.getId());
            double totalWeight = 0;
            double movingCost = 0;
            for (Integer userid :
                    userSetbyEs) {
                User user = userList.get(userid);
                //统计group中所有用户的快递费
                totalWeight += user.getWeight();
                //移动成本
                double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), e.getJingdu(), e.getWeidu());
                movingCost = Config.unitCost * dist;
            }
            //计算group的支付成本和
            double deleiverExpense = CalculateDeleiverExpense.calculate(e.getFirstPrice(), e.getContinuePrice(), e.getScale(), totalWeight, 0, 0, 0);
            totalExpressExpense+=deleiverExpense;
            //计算group中所有用户的移动成本
            totalMovingCost+=movingCost;
        }
        totalCharge = totalMovingCost+totalExpressExpense;
        System.out.println("数学优化:");
        System.out.println("平均移动成本:"+totalMovingCost/userList.size());
        System.out.println("平均快递费:"+totalExpressExpense/userList.size());
        System.out.println("平均寄件成本:"+totalCharge/userList.size());
    }


    //根据value寻找key
    public static Object getKey(Map<Integer, Double> map, double value) {
        int key = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                key = entry.getKey();
            }
        }
        return key;
    }


    //遍历寻找最小value
    public static Object gerMinValue(Map<Integer, Double> map) {
        if (map.size() == 0)
            return null;
        Collection<Double> collection = map.values();
        Object[] obj = collection.toArray();
        Arrays.sort(obj);
        return obj[0];
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
        List<Integer> numList = Lists.newArrayList( 1, 2, 3, 4, 5, 6, 7, 8, 9,10);
        fullArray(numList, prefix, result);
        System.out.println(result);
//        int partition = 3;s
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
