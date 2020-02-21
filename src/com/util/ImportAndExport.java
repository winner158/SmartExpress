package com.util;

import com.config.Config;
import com.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 导入导出接口类
 *
 * @Author: pfsun
 * @Date: 2020-01-02 10:22
 */
public class ImportAndExport {


    //导入数据
    public static String importData(String fileName) throws IOException {

        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();

        StringBuilder str = new StringBuilder();
        while (line != null) {
            //System.out.println(line);
            str.append(line+"-");
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        fileReader.close();
        return str.toString();
    }

    //导出数据
    public static void exportData(String fileName, String content) throws IOException {
        Writer fw = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
        System.out.println("写入成功!!");
    }

    public static void main(String[] args) {
        //1.加载数据
        String userinfo = null;
        try {
            userinfo = ImportAndExport.importData("data/user.txt");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        String[] userSplit = userinfo.split("-");
        System.out.println(userSplit.length);
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (String str :
                userSplit) {
            User user = new User();
            String[] rowSplit = str.split(",");
//            user.setId(count++);
//            user.setJingdu(Double.parseDouble(rowSplit[0]));
//            user.setWeidu(Double.parseDouble(rowSplit[1]));
//            user.setWeight(Double.parseDouble(rowSplit[2]));
//            user.setLengthP(Double.parseDouble(rowSplit[3]));
//            user.setWidthP(Double.parseDouble(rowSplit[4]));
//            user.setHeightP(Double.parseDouble(rowSplit[5]));
//            user.setUnitCost(Config.unitCost);
            double weight = Math.random();
            weight = Double.parseDouble(String.format("%.1f", weight));
            weight = 1 + weight;
            stringBuilder.append(rowSplit[0]+","+rowSplit[1]+","+weight+","+"10"+","+"10"+","+"10");
            stringBuilder.append("\r\n");
        }
        try {
            exportData("data/large-package-user.txt",stringBuilder.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
