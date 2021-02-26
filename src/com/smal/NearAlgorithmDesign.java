package com.smal;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;
import com.util.ImportAndExport;

import java.util.*;

/**
 * 就近寄件：就近寄件合作算法，即最低移动成本合作寄件分配算法
 * 主要思想：就近选择快递点，然后根据自己包裹的重量计算成本
 *
 * @Author: pfsun
 * @Date: 2020-01-02 11:53
 */
public class NearAlgorithmDesign {


    //计算每个用户距离每一个快递点的距离，选择距离最小的哪一个作为自己的选择.输出结果：userid=>ESId
    public static HashMap<Integer, Object> alocationMechnism(List<ExpressS> ESlist, List<User> userList) {

        HashMap<Integer, Object> hashMap = new LinkedHashMap<>();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            HashMap<Integer, Double> userSelect = new HashMap<Integer, Double>();
            for (int j = 0; j < ESlist.size(); j++) {
                ExpressS expressS = ESlist.get(j);
                double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), expressS.getJingdu(), expressS.getWeidu());
                userSelect.put(expressS.getId(), dist);
            }
            int esId = (Integer) getKey(userSelect, (double) gerMinValue(userSelect));
            hashMap.put(user.getId(), esId);
        }
        HashMap<Integer, Object> userSelectMap = new LinkedHashMap<>();
        for (ExpressS e :
                ESlist) {
            userSelectMap.put(e.getId(), new ArrayList<Integer>());
        }
        for (User user :
                userList) {
            Integer esId = (Integer) hashMap.get(user.getId());
            List<Integer> userSetbyEs = (List<Integer>) userSelectMap.get(esId);
            userSetbyEs.add(user.getId());
            userSelectMap.put(esId,userSetbyEs);
        }
        calcluateResult(userSelectMap,ESlist,userList);
        return hashMap;
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
        System.out.println("就近寄件合作算法:");
        System.out.println("平均移动成本:"+totalMovingCost/userList.size());
        System.out.println("平均快递费:"+totalExpressExpense/userList.size());
        System.out.println("平均寄件成本:"+totalCharge/userList.size());
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
//        System.out.println(re);

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
            //System.out.println("NearAlgorithmDesign:"+totalWeight);

            ExpressS express = ESlist.get(key);

            //总支付成本
            double deleiverExpenseTotal = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), totalWeight, 0, 0, 0);
            totaltotal+=deleiverExpenseTotal;
//            System.out.println("NearAlgorithmDesign:"+totaltotal);
            for (Integer userid :
                    list) {
                User user = userList.get(userid);
                double deleiverExpense = (user.getWeight()/totalWeight)*deleiverExpenseTotal;
                //移动成本
                double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), express.getJingdu(), express.getWeidu());
                double movingCost = Config.unitCost * dist;
                totalMoving+=movingCost;
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
//        System.out.println("NearAlgorithmDesign:charge-"+totaltotal);
//        System.out.println("NearAlgorithmDesign:moving-"+totalMoving);
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
