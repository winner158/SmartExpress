package com.smal;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.ImportAndExport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Main {

    public static List<User> getUserList() {
        //1.加载数据
        String userinfo = null;
        try {
            userinfo = ImportAndExport.importData("data/small-package-user.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //2.数据预处理
        String[] userSplit = userinfo.split("-");
        List<User> userlist = new ArrayList<>();
        int count = 0;
        //int start = 0;
        for (String str :
                userSplit) {
            if (count > 130)
                break;
            else {
                User user = new User();
                String[] rowSplit = str.split(",");
                user.setId(count++);
                user.setJingdu(Double.parseDouble(rowSplit[0]));
                user.setWeidu(Double.parseDouble(rowSplit[1]));
                user.setWeight(Double.parseDouble(rowSplit[2]));
                user.setLengthP(Double.parseDouble(rowSplit[3]));
                user.setWidthP(Double.parseDouble(rowSplit[4]));
                user.setHeightP(Double.parseDouble(rowSplit[5]));
                user.setUnitCost(Config.unitCost);
                userlist.add(user);
            }
        }
        return userlist;
    }

    public static List<ExpressS> getExpressSList() {
        //1.加载数据
        String Esinfo = null;
        try {
            Esinfo = ImportAndExport.importData("data/ES.txt");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //2.数据预处理
        String[] EsinfoSplit = Esinfo.split("-");
        List<ExpressS> EsinfoList = new ArrayList<>();
        int count = 0;
        int start = 0;
        for (String str :
                EsinfoSplit) {
            if (count > -1 && count <= 12) {
                count++;
                ExpressS es = new ExpressS();
                String[] rowSplit = str.split(",");
                es.setId(start++);
                es.setJingdu(Double.parseDouble(rowSplit[0]));
                es.setWeidu(Double.parseDouble(rowSplit[1]));
                es.setFirstPrice(Double.parseDouble(rowSplit[2]));
                es.setContinuePrice(Double.parseDouble(rowSplit[3]));
                es.setScale(Integer.parseInt(rowSplit[4]));
                EsinfoList.add(es);
            } else
                count++;

        }
        return EsinfoList;
    }

    public static void main(String[] args) {

        List<User> allUserList = getUserList();
        List<ExpressS> expressSList = getExpressSList();
        int count = 5;

        List<Object> timeList = new ArrayList<>();

        int number = 1;
        int iterations = 1;

        //用来统计时间
        HashMap<String, Long> timeCalculate = new HashMap<>();

        while (number <= iterations) {
            List<User> userList = new ArrayList<>();
            List<Integer> tempList = new ArrayList<Integer>();
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                int temp = random.nextInt(allUserList.size());
                if (!tempList.contains(temp)) {
                    tempList.add(temp);
                    User user = allUserList.get(temp);
                    user.setId(i);
                    userList.add(user);
                } else
                    i--;
            }

            int m = 12;
            List<ExpressS> esList = new ArrayList<>();
            List<Integer> EsList = new ArrayList<Integer>();
            for (int i = 0; i < m; i++) {
                int temp = random.nextInt(expressSList.size());
                if (!EsList.contains(temp)) {
                    EsList.add(temp);
                    ExpressS expressS = expressSList.get(temp);
                    expressS.setId(i);
                    esList.add(expressS);
                } else
                    i--;
            }

            System.out.println("es size:" + esList.size());
            System.out.println("user size:" + userList.size());

//            long startTime = System.currentTimeMillis();
//            //合作寄件博弈算法
//            System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(esList, userList));
//            long endTime = System.currentTimeMillis();
//            System.out.println("合作寄件博弈算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

            //if(number%100==0)
//            System.out.println("第" + number + "次结果");
//
//            long startTime = System.currentTimeMillis();
            //CPAA算法
//            System.out.println(CPAA.alocationMechnism(esList, userList));
//            long endTime = System.currentTimeMillis();
//            System.out.println("CPAA：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("CPAA")){
//                timeCalculate.put("CPAA",timeCalculate.get("CPAA")+(endTime - startTime));
//            }else
//                timeCalculate.put("CPAA",(endTime - startTime));
//
            //就近寄件合作算法
//            startTime = System.currentTimeMillis();
//            System.out.println(NearAlgorithmDesign.alocationMechnism(esList, userList));
//            endTime = System.currentTimeMillis();
//            System.out.println("就近寄件合作算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("nearco")){
//                timeCalculate.put("nearco",timeCalculate.get("nearco")+(endTime - startTime));
//            }else
//                timeCalculate.put("nearco",(endTime - startTime));
            //就近非合作寄件分配算法
//            startTime = System.currentTimeMillis();
//            System.out.println(NearNotCoAlgorithmDesign.alocationMechnism(esList, userList));
//            endTime = System.currentTimeMillis();
//            System.out.println("就近非合作寄件分配算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("nearnotco")){
//                timeCalculate.put("nearnotco",timeCalculate.get("nearnotco")+(endTime - startTime));
//            }else
//                timeCalculate.put("nearnotco",(endTime - startTime));
            //最低支付成本合作寄件分配算法
//            startTime = System.currentTimeMillis();
//            System.out.println(LowestChargeAlgorithmDesign.alocationMechnism(esList, userList));
//            endTime = System.currentTimeMillis();
//            System.out.println("最低支付成本合作寄件分配算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("lowcharge")){
//                timeCalculate.put("lowcharge",timeCalculate.get("lowcharge")+(endTime - startTime));
//            }else
//                timeCalculate.put("lowcharge",(endTime - startTime));
//
//
//            //最优解
            MathOptimizationAlgorithmDesign.calculateMinValue(esList, userList);
//            if(number==iterations){
//                Iterator iter = timeCalculate.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iter.next();
//                    String key = entry.getKey().toString();
//                    Long val = (Long) entry.getValue();
//                    System.out.println(key+":"+val/iterations);
//                }
//            }
            number++;
        }


        //合作寄件博弈算法
        //System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(getExpressSList(),userList));


//        switch (0){
//            case 1:
//                //CPAA算法
//                System.out.println(CPAA.alocationMechnism(getExpressSList(),userList));
//                System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(getExpressSList(),userList));
//                break;
//            case 2:
//                //合作寄件博弈算法
//                System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(getExpressSList(),userList));
//                break;
//            case 3:
//                //就近寄件合作算法
//                System.out.println(NearAlgorithmDesign.alocationMechnism(getExpressSList(),userList));
//                break;
//            case 4:
//                //就近非合作寄件分配算法
//                System.out.println(NearNotCoAlgorithmDesign.alocationMechnism(getExpressSList(),userList));
//                break;
//            case 5:
//                //最低支付成本合作寄件分配算法
//                System.out.println(LowestChargeAlgorithmDesign.alocationMechnism(getExpressSList(),userList));
//                break;
//        }
        //CPAA算法
        //System.out.println(CPAA.alocationMechnism(getExpressSList(),getUserList()));
        //合作寄件博弈算法
        //System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(getExpressSList(),getUserList()));
        //就近寄件合作算法
        //System.out.println(NearAlgorithmDesign.alocationMechnism(getExpressSList(),getUserList()));
        //就近非合作寄件分配算法
        //System.out.println(NearNotCoAlgorithmDesign.alocationMechnism(getExpressSList(),getUserList()));
        //最低支付成本合作寄件分配算法
        //System.out.println(LowestChargeAlgorithmDesign.alocationMechnism(getExpressSList(),getUserList()));
        //最优解
        //MathOptimizationAlgorithmDesign.calculateMinValue(getExpressSList(),getUserList());

    }
}
