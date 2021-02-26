package com.smal;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.CalculateDeleiverExpense;
import com.util.CalculateDistance;

import java.util.*;

/**
 *  最低支付成本合作寄件分配算法（各自选择快递费最低的ES，定价采用合作模式）
 * （用以说明我们同时考虑移动成本和快递费的优势）
 *  具体实现：选择ES的时候将优化目标修改为只优化快递费，不考虑移动成本，算法和本文提出的近似算法相同。
 *
 * @Author: pfsun
 * @Date: 2020-02-21 18:15
 */
public class LowestChargeAlgorithmDesign {

    public static HashMap<Integer, Object> alocationMechnism(List<ExpressS> ESlist, List<User> userList) {
        /**
         * 一、初始化阶段：
         */
        //1.候选集初始化（未分配的用户集合）
        List<Integer> unSelectedUserList = new ArrayList<>();
        for (User user :
                userList) {
            unSelectedUserList.add(user.getId());
        }
        //2.初始化每个快递点的用户集,都为空集合 key=>EsId value=>用户的id集合
        HashMap<Integer, Object> userSelectMap = new LinkedHashMap<>();
        for (ExpressS e :
                ESlist) {
            userSelectMap.put(e.getId(), new ArrayList<Integer>());
        }

        /**
         * 二、分配阶段：
         */
        while (unSelectedUserList.size() > 0) {

//            System.out.println("unSelectedUserList size:"+unSelectedUserList.size());

            //存储待比较的radio key=>EsId value=>比例值
            HashMap<Integer, Double> radioMap = new HashMap<Integer, Double>();
            //存储待比较的用户集合 key=>EsId value=>候选用户集合userCandidateSet,以便拿到最终的min用户集合
            HashMap<Integer, Object> userGroup = new HashMap<Integer, Object>();
            for (ExpressS e :
                    ESlist) {

                //1.首先进行二分搜索
                List<Integer> userSetbyEs = new ArrayList<Integer>((List<Integer>) userSelectMap.get(e.getId()));

//                System.out.println("before:userSetbyEs=>"+userSetbyEs);
                List<Integer> userCandidateSet = binarySearch(e, userSetbyEs, unSelectedUserList, userList);
//                System.out.println("after:userSetbyEs=>"+userSetbyEs);
                //2.重新组合上述两个集合
//                userCandidateSet.addAll(userSetbyEs);

                userSetbyEs.addAll(userCandidateSet);

                //3.计算比值
                if (userCandidateSet.size() > 0) {
                    double radioByUserCandidate = calcluateCost(userSetbyEs, userList, e) / userCandidateSet.size();
                    radioMap.put(e.getId(), radioByUserCandidate);
                    userGroup.put(e.getId(), userCandidateSet);
                }

//                System.out.println("esid:"+e.getId()+radioMap);
            }

//            System.out.println("userGroup:"+userGroup);
            //4.查找比例中最小的候选集合的key=>EsId
            int minEdId = (Integer) getKey(radioMap, (double) gerMinValue(radioMap));
//            System.out.println("minEdId:"+minEdId);
            //5.查找比例中最小的候选集合的value=>userlist
            List<Integer> optimalUserSet = (List<Integer>) userGroup.get(minEdId);
//            System.out.println("optimalUserSet:"+optimalUserSet);
            //6.将最有用户集合与对应的ES集合合并
            List<Integer> optimalUserSetByEsId = new ArrayList<Integer>((List<Integer>) userSelectMap.get(minEdId));
            optimalUserSetByEsId.addAll(optimalUserSet);
            //7.更新每个快递点的用户集userSelectMap
            userSelectMap.put(minEdId, optimalUserSetByEsId);
            //8.更新候选集（未分配的用户集合）unSelectedUserList
            unSelectedUserList.removeAll(optimalUserSet);
//            System.out.println("unSelectedUserList:"+unSelectedUserList);
        }
        System.out.println("final allocation:"+userSelectMap);
        calcluateResult(userSelectMap,ESlist,userList);
        return userSelectMap;
    }

    //二分搜索逻辑
    public static List<Integer> binarySearch(ExpressS e, List<Integer> userGroup, List<Integer> unSelectedUserList, List<User> userList) {

        List<Integer> re = new ArrayList<>();
        double low = 0;
        List<Integer> initUserGroup = new ArrayList<Integer>(userGroup);
        initUserGroup.addAll(unSelectedUserList);
        double high = calcluateCost(initUserGroup, userList, e) / unSelectedUserList.size();
        double mid = (low + high) / 2;

        int count = 0;

//        System.out.println("init high:"+high);
//
//        System.out.println("init mid:"+mid);

        while (true) {

//            System.out.println("userGroup:"+userGroup);
//
//            System.out.println("mid:"+mid);
//
//            System.out.println("执行次数："+count++);

            //存储待比较的radio key=>subSets-Id value=>marginValue
            HashMap<Integer, Double> radioMap = new HashMap<Integer, Double>();

            List<List<Integer>> subSets = getSubSets(unSelectedUserList);
            for (int i = 0; i < subSets.size(); i++) {
                List<Integer> tempUserGroup = new ArrayList<Integer>(userGroup);
                tempUserGroup.addAll(subSets.get(i));
//                System.out.println("userGroup:"+userGroup);
//                System.out.println("tempUserGroup:"+tempUserGroup);
                double marginValue = calcluateCost(tempUserGroup, userList, e) - mid * subSets.get(i).size();
                radioMap.put(i, marginValue);
                //  System.out.println("marginValue:"+marginValue);
            }

//            System.out.println("radioMap=>"+radioMap);

            //查找marginValue最小的分配集合
            int minKeyId = (Integer) getKey(radioMap, (double) gerMinValue(radioMap));
//            System.out.println("minvalue："+radioMap.get(minKeyId));
//            System.out.println("minList："+subSets.get(minKeyId));
            //查找marginValue中最小的候选集合的value=>list
            List<Integer> marginList = subSets.get(minKeyId);
            //计算合并最有集合之后的阈值
            List<Integer> afterOptionalUserGroup = new ArrayList<Integer>(userGroup);
            afterOptionalUserGroup.addAll(marginList);
            //第一条件判断
//            System.out.println("第一条件判断:"+Math.abs(calcluateCost(afterOptionalUserGroup, userList, e) / marginList.size() - mid));
            if (Math.abs(calcluateCost(afterOptionalUserGroup, userList, e) / marginList.size() - mid) < Config.threshold) {
                re = marginList;
                break;
            }
            //第二条件判断
            if ((calcluateCost(afterOptionalUserGroup, userList, e) - mid * marginList.size()) <= 0) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
//            System.out.println("mid:"+mid);
        }
        return re;
    }


    //获取集合的所有子集
    public static List<List<Integer>> getSubSets(List<Integer> unSelectedUserList) {

        List<List<Integer>> list = new ArrayList<>();

        int len = unSelectedUserList.size();
        // 获得所有子集数
        int count = (int) Math.pow(2, len);
        // 输出所有子集
        for (int i = 1; i < count; i++) {
            List<Integer> itemList = new ArrayList<>();
            // 将整数转换成字符串，如果前面为0则会被去掉，比如001,则会显示为1
            String binaryStr = Integer.toBinaryString(i);
            // 二进制字符串的长度
            int binLen = binaryStr.length() - 1;
            // 遍历二进制字符串，(每次遍历输出一个子集)
            for (int j = len - 1; j >= 0 && binLen >= 0; j--, binLen--) {
                // 二进制 数为1的，则输出对应位置的数值
                if (binaryStr.charAt(binLen) == '1') {
                    itemList.add(unSelectedUserList.get(j));
                }
            }
            list.add(itemList);
        }
//        System.out.println(list);
        return list;
    }



    //计算group成本
    public static double calcluateCost(List<Integer> groupUserList, List<User> userList, ExpressS expressS) {

        double totalFare = 0;
        double totalWeight = 0;
        for (Integer userid :
                groupUserList) {
            User user = userList.get(userid);
            //移动成本
//            double dist = CalculateDistance.distanceOfTwoPoints(user.getJingdu(), user.getWeidu(), expressS.getJingdu(), expressS.getWeidu());
//            double movingCost = Config.unitCost * dist;
//            totalFare += movingCost;
            //包裹重量累积
            totalWeight += user.getWeight();
        }
        totalFare += CalculateDeleiverExpense.calculate(expressS.getFirstPrice(), expressS.getContinuePrice(), expressS.getScale(), totalWeight, 0, 0, 0);
        return totalFare;
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
        System.out.println("最低支付成本合作寄件分配算法:");
        System.out.println("平均移动成本:"+totalMovingCost/userList.size());
        System.out.println("平均快递费:"+totalExpressExpense/userList.size());
        System.out.println("平均寄件成本:"+totalCharge/userList.size());
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
