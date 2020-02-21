package com.large;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;

import java.util.*;

/**
 * 基于联盟顺序的合作寄件博弈算法
 *
 * @Author: pfsun
 * @Date: 2020-01-02 11:56
 */
public class CoGameBasedCoalitionOrderAlgorithmDesign {


    public static HashMap<Integer, Object> alocationMechnism(List<ExpressS> ESlist, List<User> userList) {
        //1.初始化：每个用户计算就近的快递点，加入，存在一个成本（包含移动成本和支付成本）
        HashMap<Integer, Object> InitUserAllocation = NearNotCoAlgorithmDesign.alocationMechnism(ESlist, userList);
//        HashMap<Integer, Object> InitUserAllocation = LowestChargeAlgorithmDesign.alocationMechnism(ESlist, userList);
        System.out.println(InitUserAllocation);
        HashMap<Integer, Object> comparedMap = (HashMap<Integer, Object>) InitUserAllocation.clone();//待比较的map

        //统计最终结果:总成本
        HashMap<Integer, Double> resultMap = new HashMap<>();

        int NumberIterations = 100000;
        int IterationsCount = 1;
        while (NumberIterations > 0) {
            System.out.println("迭代次数：" + IterationsCount);
            //更新策略
            HashMap<Integer, Object> reverseMap = updateUserSelection(InitUserAllocation, ESlist);
            double currentTotalCost = calcluateTotalCost(reverseMap, ESlist, userList); //当前的成本值

//            System.out.println("总成本为：" + calcluateTotalCost(reverseMap, ESlist, userList));
            //2.对所有快递点（联盟）进行遍历，随机选择一个用户，计算机效用最大的一个联盟（快递点）加入
            //Collections.reverse(userList);
            for (int i = 0; i < userList.size(); i++) {
//                System.out.println(i+"-map:"+reverseMap);
//                System.out.println("用户id："+i);
                User user = userList.get(i);
                //计算当前的总成本
                int esId = (int) InitUserAllocation.get(user.getId());
                ExpressS expressS = ESlist.get(esId);
                List<Integer> userIdList = (List<Integer>) reverseMap.get(esId);

                List<User> userlist = new ArrayList<>();

                double weight = 0;//统计原联盟用户包裹的总重量
                for (int k = 0; k < userIdList.size(); k++) {
                    Integer userId = userIdList.get(k);
                    User userTemp = userList.get(userId);
                    weight += userTemp.getWeight();
                    userlist.add(userTemp);
                }

                double userOldMoving = user.getUnitCost() * CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), expressS.getJingdu(), expressS.getWeidu());
                double beforeJoinTotalCharge = CalculateDeleiverExpense.calculate(expressS.getFirstPrice(), expressS.getContinuePrice(), expressS.getScale(), weight, 0, 0, 0);
                double userOldCharege = user.getWeight() / weight * beforeJoinTotalCharge;
                //System.out.println("移动成本："+userOldMoving+" 支付成本："+userOldCharege);
                //候选集合
                HashMap<Integer, Double> userSelect = new HashMap<Integer, Double>();
                for (int j = 0; j < ESlist.size(); j++) {
                    if (j != (Integer) InitUserAllocation.get(i)) {
                        List<Integer> newUserIdlist = (List<Integer>) reverseMap.get(j);
                        List<User> newUserlist = new ArrayList<>();
                        for (Integer useridbyEs :
                                newUserIdlist) {
                            newUserlist.add(userList.get(useridbyEs));
                        }
                        //System.out.println("当前ES id："+j+" 效用函数为："+utilityFunction(userlist, ESlist.get(j), user, userOldCharege, userOldMoving));
                        if (isGameTransferFeasible(newUserlist, ESlist.get(j), user, userOldCharege, userOldMoving)) {
                            //&& (Integer) InitUserAllocation.get(i) != j
                            //可以转换
                            double temp = utilityFunction(newUserlist, ESlist.get(j), user, userOldCharege, userOldMoving);
                            userSelect.put(j, temp);
                            //System.out.println("当前ES id为："+j+" 当前的效用函数为："+temp);
                        }
                    }

                }
                if (userSelect.size() > 0) {
                    int maxEsId = (Integer) getKey(userSelect, (double) gerMaxValue(userSelect));
                    System.out.println("最大的ES id：" + maxEsId);
//                    System.out.println(userSelect.get(maxEsId));
                    //更新用户选择的策略
                    InitUserAllocation.put(user.getId(), maxEsId);
                    //在旧联盟中删除改用户的id
                    reverseMap = updateUserSelection(InitUserAllocation, ESlist);
                    // ((List<Integer>) reverseMap.get(esId)).remove(index);
                    //在新联盟中增加该用户id
                    // ((List<Integer>) reverseMap.get(maxEsId)).add(user.getId());

                } else
                    break;
//                System.out.println(reverseMap);
            }
            //   currentTotalCost =(double) Math.round(calcluateTotalCost(reverseMap, ESlist, userList) * 10000) / 10000 ;
            currentTotalCost = calcluateTotalCost(reverseMap, ESlist, userList);
//            System.out.println("userseletion:" + InitUserAllocation.get(0));
            if (isEqualsTwoMap(InitUserAllocation, comparedMap) || isTerminal(resultMap, currentTotalCost))
                NumberIterations = 0;
            else {
                NumberIterations--;
                //currentTotalCost = calcluateTotalCost(reverseMap, ESlist, userList);
                resultMap.put(IterationsCount++, currentTotalCost);
            }
            comparedMap = (HashMap<Integer, Object>) InitUserAllocation.clone();
            System.out.println(resultMap);
            System.out.println("userseletion:" + InitUserAllocation);
            //计算总的成本和
            System.out.println("总成本为：" + currentTotalCost);
        }

        //3.观察策略是否变化
        return comparedMap;
    }


    //效用函数
    public static double utilityFunction(List<User> newCoalition, ExpressS newExpressS, User newUser, double userOldCharege, double userOldMoving) {

        double weight = 0;
        double movingCost = 0;
        for (User user :
                newCoalition) {
            weight += user.getWeight();
            movingCost += user.getUnitCost() * CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), newExpressS.getJingdu(), newExpressS.getWeidu());
        }
        //联盟中其他用户的支付成本：该用户加入之前，该用户加入之后
        double beforeJoinTotalCharge = CalculateDeleiverExpense.calculate(newExpressS.getFirstPrice(), newExpressS.getContinuePrice(), newExpressS.getScale(), weight, 0, 0, 0);
        double afterJoinTotalChargeIncludingMe = CalculateDeleiverExpense.calculate(newExpressS.getFirstPrice(), newExpressS.getContinuePrice(), newExpressS.getScale(), weight + newUser.getWeight(), 0, 0, 0);
        //加入之后该用户效用的变化
        double afterJoinNewUserCharge = newUser.getWeight() / (weight + newUser.getWeight()) * afterJoinTotalChargeIncludingMe;
        double afterJoinTotalCharge = afterJoinTotalChargeIncludingMe - afterJoinNewUserCharge;

        double userNewMoving = newUser.getUnitCost() * CalculateDistance.distanceOfTwoPoints(newUser.getJingdu(), newUser.getWeidu(), newExpressS.getJingdu(), newExpressS.getWeidu());

        //用户加入该联盟之后，其余用户效用变化的和，移动成本相当于没变化
        double utility = (beforeJoinTotalCharge - afterJoinTotalCharge) + userOldCharege - afterJoinNewUserCharge + userOldMoving - userNewMoving;
//        System.out.println("用户" + newUser.getId() + "加入联盟" + newExpressS.getId() + "之后的效用：" + utility);
        return utility;
    }

    //判断是否终止
    public static Boolean isTerminal(HashMap<Integer, Double> hashMap, double value) {

        if (hashMap.size() <= 0)
            return false;
        int minKey = (Integer) getKey(hashMap, (double) gerMinValue(hashMap));
        for (Map.Entry<Integer, Double> entry : hashMap.entrySet()) {
            System.out.println("key=" + minKey + "-" + entry.getKey());
            if (value == entry.getValue() && entry.getKey().equals(minKey)) {
                return true;
            }
        }
        return false;

    }


    //判断是否可以加入该联盟
    public static Boolean isGameTransferFeasible(List<User> newCoalition, ExpressS newExpressS, User newUser, double userOldCharege, double userOldMoving) {

        //|| currentTotalCost <= calcluateTotalCost(reverseMap, ESlist, userList)
        if (utilityFunction(newCoalition, newExpressS, newUser, userOldCharege, userOldMoving) > 0)
            return true;
        else
            return false;
    }

    //遍历寻找最大value
    public static Object gerMaxValue(Map<Integer, Double> map) {
        if (map.size() == 0)
            return null;
        Collection<Double> collection = map.values();
        Object[] obj = collection.toArray();
        Arrays.sort(obj);
        return obj[obj.length - 1];
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


    public static HashMap<Integer, Object> updateUserSelection(HashMap<Integer, Object> InitUserAllocation, List<ExpressS> ESlist) {
        HashMap<Integer, Object> reverseMap = new LinkedHashMap<>();
        for (int i = 0; i < ESlist.size(); i++) {

            List<Integer> list = new ArrayList<>();
            Iterator<Integer> keys = InitUserAllocation.keySet().iterator();
            while (keys.hasNext()) {
                int key = keys.next();
                Integer value = (Integer) InitUserAllocation.get(key);
                if (value == ESlist.get(i).getId()) {
                    list.add(key);
                }
            }
            reverseMap.put(ESlist.get(i).getId(), list);
        }
        return reverseMap;
    }

    //计算总成本
    public static double calcluateTotalCost(HashMap<Integer, Object> reverseMap, List<ExpressS> ESlist, List<User> userList) {

        double totalCost = 0;
        for (Map.Entry<Integer, Object> entry : reverseMap.entrySet()) {
            ExpressS expressS = ESlist.get(entry.getKey());
            List<Integer> useridList = (List<Integer>) entry.getValue();
            double countWeightPerEs = 0;
            double countMovingCostPerEsUsers = 0;
            for (Integer userid :
                    useridList) {
                countWeightPerEs += userList.get(userid).getWeight();
                double dist = CalculateDistance.distanceOfTwoPoints(userList.get(userid).getJingdu(), userList.get(userid).getWeidu(), expressS.getJingdu(), expressS.getWeidu());
                countMovingCostPerEsUsers = Config.unitCost * dist;
            }
            double countDeliverCostPerEsUsers = CalculateDeleiverExpense.calculate(expressS.getFirstPrice(), expressS.getContinuePrice(), expressS.getScale(), countWeightPerEs, 0, 0, 0);
            ;
            totalCost += countMovingCostPerEsUsers + countDeliverCostPerEsUsers;
        }
        return totalCost;
    }

    //判断两个map是否相同
    public static Boolean isEqualsTwoMap(HashMap<Integer, Object> hashMapS, HashMap<Integer, Object> hashMapT) {
//        System.out.println("比较的map：");
//        System.out.println(hashMapS);
//        System.out.println(hashMapT);
        for (Map.Entry<Integer, Object> entry : hashMapS.entrySet()) {
            // System.out.println(entry.getValue()+"-"+hashMapT.get(entry.getKey()));
            if (!entry.getValue().equals(hashMapT.get(entry.getKey()))) {
//                System.out.println("用户：" + entry.getKey() + "策略变化了");

                return false;
            }
        }
        return true;
    }

}
