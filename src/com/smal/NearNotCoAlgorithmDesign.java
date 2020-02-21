package com.smal;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;
import com.util.ImportAndExport;

import java.util.*;

/**
 * 就近非合作寄件分配算法
 * 主要思想：就近选择快递点，然后根据自己包裹的重量计算成
 * @Author: pfsun
 * @Date: 2020-01-02 11:56
 */
public class NearNotCoAlgorithmDesign {

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
            //System.out.println("NearAlgorithmDesign:"+totalWeight);

            ExpressS express = ESlist.get(key);

            for (Integer userid :
                    list) {
                User user = userList.get(userid);
                double deleiverExpense = CalculateDeleiverExpense.calculate(express.getFirstPrice(), express.getContinuePrice(), express.getScale(), user.getWeight(), user.getLengthP(), user.getWidthP(), user.getHeightP());
                totaltotal+=deleiverExpense;
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
        System.out.println("NearNotCoAlgorithmDesign:charge-"+totaltotal);
        System.out.println("NearNotCoAlgorithmDesign:moving-"+totalMoving);
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
