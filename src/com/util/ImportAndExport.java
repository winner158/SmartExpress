package com.util;

import java.io.*;
import java.util.List;

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

}
