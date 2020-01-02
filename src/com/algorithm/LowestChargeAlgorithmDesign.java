package com.algorithm;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;
import com.util.ImportAndExport;

import java.util.*;

/**
 * 最低支付成本算法
 * 主要思想：按照最低成本贪心选择
 *
 * @Author: pfsun
 * @Date: 2020-01-02 11:54
 */
public class LowestChargeAlgorithmDesign {

    //计算每个用户加入每一个快递点后，总成本的变化，选择加入后总成本变化最小的快递点作为自己的策略.输出结果：userid=>ESId
    public static HashMap<Integer, Object> alocationMechnism(List<ExpressS> ESlist, List<User> userList) {

        double totalCharge = 0;
        //统计每一个快递点的包裹的总重量
        HashMap<Integer, Double> EsPackageW = new HashMap<Integer, Double>();
        HashMap<Integer, Double> userSelect = new HashMap<Integer, Double>();
        HashMap<Integer, Object> hashMap = new LinkedHashMap<>();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            double totalWeight = 0;
            for (int j = 0; j < ESlist.size(); j++) {
                ExpressS express = ESlist.get(j);
                if (EsPackageW.containsKey(j)) {
                    totalWeight = EsPackageW.get(j) + user.getWeight();
                    double deleiverExpenseTotal = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), totalWeight, 0, 0, 0);
                    userSelect.put(j, deleiverExpenseTotal);
                } else {
                    totalWeight = user.getWeight();
                    double deleiverExpenseTotal = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), user.getWeight(), 0, 0, 0);
                    userSelect.put(j, deleiverExpenseTotal);
                }
            }
            int esId = (Integer) getKey(userSelect, (double) gerMinValue(userSelect));
            totalCharge = userSelect.get(esId);
            //System.out.println(i+":"+totalCharge);
            EsPackageW.put(esId, totalWeight);
            hashMap.put(user.getId(), esId);
        }
        return hashMap;
    }

    //输出结果到文件，格式：userid,ESID,moving cost,charge cost, deliver cost
    public static void calcluateCost(List<ExpressS> ESlist, List<User> userList, String filename) {


        StringBuilder str = new StringBuilder();
        HashMap<Integer, Object> hashMap = alocationMechnism(ESlist, userList);

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

        double totaltotal =0;
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
            System.out.println("LowestChargeAlgorithmDesign:"+deleiverExpenseTotal);
            totaltotal+=deleiverExpenseTotal;
            for (Integer userid :
                    list) {
                User user = userList.get(userid);
                //用户的支付成本
                double deleiverExpense = (user.getWeight()/totalWeight)*deleiverExpenseTotal;
                //移动成本
                double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), express.getJingdu(), express.getWeidu());
                double movingCost = Config.unitCost * dist;
                totalMoving+=movingCost;
                //总成本
                double total = deleiverExpense + movingCost;
                str.append((user.getId()+1) + "," + (key+1) + "," + deleiverExpense + "," + movingCost + "," + total + "\r\n");
            }
        }
        try {
            ImportAndExport.exportData(filename, str.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("LowestChargeAlgorithmDesign:charge-"+totaltotal);
        System.out.println("LowestChargeAlgorithmDesign:moving-"+totalMoving);
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

}
