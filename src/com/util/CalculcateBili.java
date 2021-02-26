package com.util;

/**
 * @Author: pfsun
 * @Date: 2/7/21 4:07 PM
 */
public class CalculcateBili {


    public static void main(String[] args) {

        double[] CPAA = {7.59, 9.267, 11.48, 13.70, 14.92};

        double[] CPAGA = {8.2, 9.83, 11.98, 14.14, 15.41};

        double[] BC = {9.301083263364125, 10.81122923336141, 12.4998781944444, 14.6250249929794, 16.2651703265766};

        double[] NC = {9.53, 10.916, 13.08, 15.15, 16.13};

        double[] NN = {12.20, 13.58, 15.75, 17.823, 18.8};

        double[] NNExamTwo = {20.16, 19.17, 18.62, 17.51, 15.97};

        double[] NNExamThree = {15.82, 15.46, 15.78, 15.75};

        double[] CPAAExamTwo = {14.54, 13.76, 13.71, 12.79, 11.75};

        double[] CPAAExamThree = {13.52, 12.13, 11.74, 11.48};

        double[] CPAGAExamTwo = {15.74, 14.83, 14.78, 14.0, 13.06};

        double[] CPAGAExamThree = {14.37, 12.91, 12.22, 11.98};

        double averageComprehensiveCostByBC = 0;

        double averageComprehensiveCostByNC = 0;

        double averageComprehensiveCostByNN = 0;

        double averageGapByCPAAandCPAGA = 0;

        for (int i = 0; i < 5; i++) {

            averageComprehensiveCostByBC += Math.abs(BC[i] - CPAA[i]) / BC[i];

            averageComprehensiveCostByNC += Math.abs(NC[i] - CPAA[i]) / NC[i];

            averageComprehensiveCostByNN += Math.abs(NN[i] - CPAA[i]) / NN[i];

            averageGapByCPAAandCPAGA += Math.abs(CPAGA[i] - CPAA[i]) / CPAGA[i];


        }

        averageComprehensiveCostByBC = averageComprehensiveCostByBC / 5;

        averageComprehensiveCostByNC = averageComprehensiveCostByNC / 5;

        averageComprehensiveCostByNN = averageComprehensiveCostByNN / 5;

        averageGapByCPAAandCPAGA = averageGapByCPAAandCPAGA / 5;

        System.out.println("平均性能表现BC:" + averageComprehensiveCostByBC);


        System.out.println("平均性能表现NC:" + averageComprehensiveCostByNC);


        System.out.println("平均性能表现NN:" + averageComprehensiveCostByNN);


        System.out.println("平均性能表现CPAA&CPAGA:" + averageGapByCPAAandCPAGA);


        double[] CPAACompareOpt = {16.001020000000431, 15.635070776973543, 15.437206024646541, 14.954653873980703, 13.202678103771695};

        double[] CPAGACompareOpt = {17.923705361562368, 16.324674984115429, 16.243957522124533, 15.580881732169167, 15.1103962221809045};

        double[] OPT = {13.62126609168169, 11.331168329378419, 10.701027739765403, 10.064877648368058, 9.825271448335224};

        double averageGapByCPAAandOPT = 0;

        averageGapByCPAAandCPAGA = 0;

        double averageGapByCPAGAandOPT = 0;

        for (int i = 0; i < 5; i++) {

            averageGapByCPAAandOPT += Math.abs(CPAACompareOpt[i] - OPT[i]) / OPT[i];

            averageGapByCPAAandCPAGA += Math.abs(CPAGACompareOpt[i] - CPAACompareOpt[i]) / CPAACompareOpt[i];

            averageGapByCPAGAandOPT += Math.abs(CPAGACompareOpt[i] - OPT[i]) / OPT[i];

        }

        averageGapByCPAAandOPT = averageGapByCPAAandOPT / 5;

        averageGapByCPAAandCPAGA = averageGapByCPAAandCPAGA / 5;

        averageGapByCPAGAandOPT = averageGapByCPAGAandOPT / 5;

        System.out.println("-------------------------");

        System.out.println("平均性能表现CPAA&OPT:" + averageGapByCPAAandOPT);

        System.out.println("平均性能表现CPAA&CPAGA:" + averageGapByCPAAandCPAGA);

        System.out.println("平均性能表现CPAGAA&OPT:" + averageGapByCPAGAandOPT);


        System.out.println("-------------------------");


        double CPAACO = 0;

        double CPAGACO = 0;


        System.out.println("--------实验一-----------");

        for (int i = 0; i < 5; i++) {

            CPAACO += Math.abs(NN[i] - CPAA[i]) / NN[i];

            CPAGACO += Math.abs(NN[i] - CPAGA[i]) / NN[i];

        }

        double CPAACObili = CPAACO / 5;

        double CPAGACObili = CPAGACO / 5;

        System.out.println("CPAA性能比较：" + CPAACO / 5);

        System.out.println("CPAGA性能比较：" + CPAGACO / 5);

        System.out.println("--------实验二-----------");

        CPAACO = 0;

        CPAGACO = 0;

        for (int i = 0; i < 5; i++) {

            CPAACO += Math.abs(NNExamTwo[i] - CPAAExamTwo[i]) / NNExamTwo[i];

            CPAGACO += Math.abs(NNExamTwo[i] - CPAGAExamTwo[i]) / NNExamTwo[i];

        }

        CPAACObili += CPAACO / 5;

        CPAGACObili += CPAGACO / 5;

        System.out.println("CPAA性能比较：" + CPAACO / 5);

        System.out.println("CPAGA性能比较：" + CPAGACO / 5);

        System.out.println("--------实验三-----------");

        CPAACO = 0;

        CPAGACO = 0;

        for (int i = 0; i < 4; i++) {

            CPAACO += Math.abs(NNExamThree[i] - CPAAExamThree[i]) / NNExamThree[i];

            CPAGACO += Math.abs(NNExamThree[i] - CPAGAExamThree[i]) / NNExamThree[i];

        }

        CPAACObili += CPAACO / 5;

        CPAGACObili += CPAGACO / 5;

        System.out.println("CPAA性能比较：" + CPAACO / 5);

        System.out.println("CPAGA性能比较：" + CPAGACO / 5);

        System.out.println("--------性能比较-----------");

        CPAACObili /= 3;

        CPAGACObili /=3;

        System.out.println("re:CPAA性能比较：" + CPAACObili);

        System.out.println("re:CPAGA性能比较：" + CPAGACObili);


        System.out.println("--------CPCM性能比较-----------");

        double[] CPCM = {17.54, 16.76, 15.71, 15.79, 14.55};

        double[] NCC = {18.6368250083731, 17.8650535504296, 17.5476473601587, 16.092047546373, 15.2359586657861};

        double[] OPTCPCM = {16.963892497637, 15.14, 14.767, 14.05, 13.59};

        double CPCMCO = 0;

        double CPCMOPT = 0;


        for (int i = 0; i < 4; i++) {

            CPCMCO += Math.abs(NCC[i] - CPCM[i]) / NCC[i];
            CPCMOPT += Math.abs(OPTCPCM[i] - CPCM[i]) / OPTCPCM[i];

        }

        CPCMCO /= 5;

        CPCMOPT /= 5;


        System.out.println("CPCM性能比较：" + CPCMCO);

        System.out.println("OPT性能比较：" + CPCMOPT);

    }
}
