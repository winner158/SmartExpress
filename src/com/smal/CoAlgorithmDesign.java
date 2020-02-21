package com.smal;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;
import com.util.ImportAndExport;

import java.util.*;

/**
 * 合作寄件算法设计-近似算法
 *
 * @Author: pfsun
 * @Date: 2020-01-02 11:47
 */
public class CoAlgorithmDesign {


    /**
     * 输入数据集
     * {
     * userid：{ESid:possiblity value,……},
     * userid：{ESid:possiblity value,……},
     * ……
     * }
     */
    public static HashMap<Integer, Object> greedySelection(HashMap<Integer, Object> RelSolution, List<ExpressS> ESlist, List<User> userList) {

        HashMap<Integer, Object> userSelectMap = new LinkedHashMap<>();
        //候选集
        List<User> unSelectedUserList = userList;
        while (unSelectedUserList.size() > 0) {
            //1.先获取theta的值
            //存储l^i
            HashMap<Integer, Integer> capacityMap = new HashMap<>();
            HashMap<Integer, Object> thetaMap = new HashMap<>();
            for (ExpressS e :
                    ESlist) {
                //保证元素自然的顺序：TreeSet
                Set<Double> thetaSet = new TreeSet<>();
                for (User user :
                        unSelectedUserList) {
                    HashMap<Integer, Double> unSelectedUserPossibility = (HashMap<Integer, Double>) RelSolution.get(user.getId());
                    thetaSet.add(unSelectedUserPossibility.get(e.getId()));
                }
                int capacity = thetaSet.size();
                capacityMap.put(e.getId(), capacity);//存储theta set的size
                thetaMap.put(e.getId(), thetaSet);//存储theta值
                System.out.println(thetaSet);
            }
            //2.获取最大值
            for (ExpressS e :
                    ESlist) {
                //存储待比较的radio
                HashMap<Double, Double> radioMap = new HashMap<Double, Double>();
                //存储thate:userGroupByEs,以便拿到最终的min用户集合
                HashMap<Double, Object> thateUserGroup = new HashMap<Double, Object>();

                Set<Double> thetaSetByEs = (Set<Double>) thetaMap.get(e.getId());
                Iterator<Double> it = thetaSetByEs.iterator();
                for (int i = 0; i < capacityMap.get(e.getId()); i++) //循环theta
                {
                    List<Integer> userGroupByEs = new ArrayList<>();//用于存放g(i,thate)的用户集合
                    Double thateIndex = it.next();
                    //查找满足条件的用户集
                    for (User user :
                            unSelectedUserList) {
                        HashMap<Integer, Double> unSelectedUserPossibility = (HashMap<Integer, Double>) RelSolution.get(user.getId());
                        if (unSelectedUserPossibility.get(e.getId()) >= thateIndex) {
                            userGroupByEs.add(user.getId());//将满足条件的用户加入group
                        }
                    }
                    if (userGroupByEs.size() > 0) {
                        //计算单位成本
                        double radio = calcluateCost(userGroupByEs, userList, e) / userGroupByEs.size();
                        radioMap.put(thateIndex, radio);
                        thateUserGroup.put(thateIndex, userGroupByEs);
                    }
                }
                //取单位成本的最小值
                double thateI = (Double) getKey(radioMap, (double) gerMinValue(radioMap));
                List<Integer> list = (List<Integer>) thateUserGroup.get(thateI);
                //更新unSelectedUserList
                //更新ES的用户集合
                for (Integer userid :
                        list) {
                    userSelectMap.put(userid, e.getId());//增加用户策略
                    unSelectedUserList.remove(userid);//候选集中删除该用户
                }
            }
        }
        return userSelectMap;
    }

    //输出结果到文件，格式：userid,ESID,moving cost,charge cost, deliver cost
    public static void exportResult(HashMap<Integer, Object> RelSolution, List<ExpressS> ESlist, List<User> userList, String filename) {


        StringBuilder str = new StringBuilder();
        HashMap<Integer, Object> hashMap = greedySelection(RelSolution, ESlist, userList);

        HashMap<Integer, Object> re = new LinkedHashMap<>();
        for (int i = 0; i < ESlist.size(); i++) {

            List<Integer> list = new ArrayList<>();
            Iterator<Integer> keys = hashMap.keySet().iterator();
            while (keys.hasNext()) {
                int key = keys.next();
                Integer value = (Integer) hashMap.get(key);
                if (value == ESlist.get(i).getId()) {
                    list.add(key);
                }
            }
            re.put(ESlist.get(i).getId(), list);
        }
        System.out.println(re);

        double totaltotal = 0;
        double totalMoving = 0;
        //计算包裹重量，并输出
        Iterator<Integer> keys = re.keySet().iterator();
        while (keys.hasNext()) {
            int key = keys.next();
            //List<Integer> list = re.get(key).;
            List<Integer> list = (List<Integer>) re.get(key);
            //System.out.println(re.get(key));
            double totalWeight = 0;
            for (Integer userid :
                    list) {
                User user = userList.get(userid);
                totalWeight += user.getWeight();
            }
            //System.out.println("LowestChargeAlgorithmDesign:"+totalWeight);

            ExpressS express = ESlist.get(key);

            //总支付成本
            double deleiverExpenseTotal = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), totalWeight, 0, 0, 0);
            System.out.println("CoAlgorithmDesign:" + deleiverExpenseTotal);
            totaltotal += deleiverExpenseTotal;
            for (Integer userid :
                    list) {
                User user = userList.get(userid);
                //用户的支付成本
                double deleiverExpense = (user.getWeight() / totalWeight) * deleiverExpenseTotal;
                //移动成本
                double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), express.getJingdu(), express.getWeidu());
                double movingCost = Config.unitCost * dist;
                totalMoving += movingCost;
                //总成本
                double total = deleiverExpense + movingCost;
                str.append((user.getId()) + "," + (key) + "," + deleiverExpense + "," + movingCost + "," + total + "\r\n");
            }
        }
        try {
            ImportAndExport.exportData(filename, str.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("CoAlgorithmDesign:charge-" + totaltotal);
        System.out.println("CoAlgorithmDesign:moving-" + totalMoving);
    }


    //计算group成本
    public static double calcluateCost(List<Integer> groupUserList, List<User> userList, ExpressS expressS) {

        double totalFare = 0;
        double totalWeight = 0;
        for (Integer userid :
                groupUserList) {
            User user = userList.get(userid);
            //移动成本
            double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), expressS.getJingdu(), expressS.getWeidu());
            double movingCost = Config.unitCost * dist;
            totalFare += movingCost;
            //包裹重量累积
            totalWeight += user.getWeight();
        }
        totalFare += CalculateDeleiverExpense.calculate(expressS.getFirstPrice(), expressS.getContinuePrice(), expressS.getScale(), totalWeight, 0, 0, 0);
        return totalFare;
    }

    //遍历寻找最小value
    public static Object gerMinValue(Map<Double, Double> map) {
        if (map.size() == 0)
            return null;
        Collection<Double> collection = map.values();
        Object[] obj = collection.toArray();
        Arrays.sort(obj);
        return obj[0];
    }

    //根据value寻找key
    public static Object getKey(Map<Double, Double> map, double value) {
        double key = 0;
        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            if (value == entry.getValue()) {
                key = entry.getKey();
            }
        }
        return key;
    }


}
