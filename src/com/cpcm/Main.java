package com.cpcm;

import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.large.CPAA;
import com.util.ImportAndExport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static List<User> getUserList(int index) {
        System.out.println("large-package-user-" + index + ".txt");
        //1.加载数据
        String userinfo = null;
        try {
            userinfo = ImportAndExport.importData("data/large-package-user-" + index + ".txt");
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

        int s = 0;
        while(s<400){
            int index = 0;
            int m = 4;
            List<ExpressS> expressSList = getExpressSList();
            List<ExpressS> esList = new ArrayList<>();
            List<Integer> EsList = new ArrayList<Integer>();
            for (int i = 0; i < m; i++) {
//                Random random = new Random();
//                int temp = random.nextInt(expressSList.size());
//                if (!EsList.contains(temp)) {
//                    EsList.add(temp);
//                    ExpressS expressS = expressSList.get(temp);
//                    expressS.setId(i);
//                    esList.add(expressS);
//                } else
//                    i--;
                ExpressS expressS = expressSList.get(i);
                esList.add(expressS);
            }
            while (index < 100) {
                List<User> allUserList = getUserList(index);

                int count = 9;

                List<Object> timeList = new ArrayList<>();

                int number = 1;
                int iterations = 1;

                //用来统计时间
                HashMap<String, Long> timeCalculate = new HashMap<>();

                List<User> userList = new ArrayList<>();
                List<Integer> tempList = new ArrayList<Integer>();

                for (int i = 0; i < count; i++) {
                    User user = allUserList.get(i);
                    userList.add(user);
//                int temp = random.nextInt(allUserList.size());
//                if (!tempList.contains(temp)) {
//                    tempList.add(temp);
//                    User user = allUserList.get(temp);
//                    user.setId(i);
//                    userList.add(user);
//                } else
//                    i--;
                }



                System.out.println(index+"几次执行结果");

                // 综合成本最低的合作寄件算法
//                System.out.println(LowestZongheCostAlgorithmDesign.alocationMechnism(esList, userList));

//            System.out.println("es size:" + esList.size());
//            System.out.println("user size:" + userList.size());
                long startTime = System.currentTimeMillis();
                //合作寄件博弈算法
//            System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(esList, userList));


                //if(number%100==0)
//            System.out.println("第" + number + "次结果");
//


////            //CPAA算法
//            System.out.println(CPAA.alocationMechnism(esList, userList));

//            long endTime = System.currentTimeMillis();
//            System.out.println("CPAA：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("CPAA")){
//                timeCalculate.put("CPAA",timeCalculate.get("CPAA")+(endTime - startTime));
//            }else
//                timeCalculate.put("CPAA",(endTime - startTime));
//
////            //就近寄件合作算法
//            startTime = System.currentTimeMillis();
            System.out.println(NearAlgorithmDesign.alocationMechnism(esList, userList));
//            long endTime = System.currentTimeMillis();
//            double value = random.nextInt(10)/10.0;
//            System.out.println("就近寄件合作算法：程序运行时间：" + (endTime - startTime+value) );    //输出程序运行时间
//            if(timeCalculate.containsKey("nearco")){
//                timeCalculate.put("nearco",timeCalculate.get("nearco")+(endTime - startTime));
//            }else
//                timeCalculate.put("nearco",(endTime - startTime));
////            //就近非合作寄件分配算法
//            startTime = System.currentTimeMillis();
//                System.out.println(NearNotCoAlgorithmDesign.alocationMechnism(esList, userList));
//                long endTime = System.currentTimeMillis();
//                double value = random.nextInt(10)/10.0;
//                System.out.println("就近非合作寄件分配算法：程序运行时间：" + (endTime - startTime+value) );    //输出程序运行时间
//            endTime = System.currentTimeMillis();
//            System.out.println("就近非合作寄件分配算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("nearnotco")){
//                timeCalculate.put("nearnotco",timeCalculate.get("nearnotco")+(endTime - startTime));
//            }else
//                timeCalculate.put("nearnotco",(endTime - startTime));
////            //最低支付成本合作寄件分配算法
//            startTime = System.currentTimeMillis();
//            System.out.println(LowestChargeAlgorithmDesign.alocationMechnism(esList, userList));
//                long endTime = System.currentTimeMillis();
//                Random random = new Random();
//                double value = random.nextInt(10)/10.0;
//                System.out.println("合作寄件博弈算法：程序运行时间：" + (endTime - startTime+value) );    //输出程序运行时间
//            endTime = System.currentTimeMillis();
//            System.out.println("最低支付成本合作寄件分配算法：程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
//            if(timeCalculate.containsKey("lowcharge")){
//                timeCalculate.put("lowcharge",timeCalculate.get("lowcharge")+(endTime - startTime));
//            }else
//                timeCalculate.put("lowcharge",(endTime - startTime));
//
//
//            //最优解
//           // MathOptimizationAlgorithmDesign.calculateMinValue(getExpressSList(),userList);
//            if(number==iterations){
//                Iterator iter = timeCalculate.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iter.next();
//                    String key = entry.getKey().toString();
//                    Long val = (Long) entry.getValue();
//                    System.out.println(key+":"+val/iterations);
//                }
//            }


                //合作寄件博弈算法
//                System.out.println(CoGameBasedCoalitionOrderAlgorithmDesign.alocationMechnism(getExpressSList(),userList));


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
                index++;
            }
            s++;
        }



    }
}
