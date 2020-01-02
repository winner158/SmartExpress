package com.util;

/**
 * 计算两点之间的距离
 *
 * @Author: pfsun
 * @Date: 2020-01-02 11:59
 */
public class CalculateDistance {


    // 地球半径
    private static final double EARTH_RADIUS = 6370996.81;

    // 弧度
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    public static double distanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = radian(lat1);
        double radLat2 = radian(lat2);
        double a = radLat1 - radLat2;
        double b = radian(lng1) - radian(lng2);
        double dist = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        dist = dist * EARTH_RADIUS;
        dist = Math.round(dist * 10000) / 10000;
        //double ss = s * 1.0936132983377;
        //System.out.println("两点间的距离是：" + dist + "米");
        return dist;
    }

    public static void main(String[] args) {
        distanceOfTwoPoints(23.5539530, 114.8903920, 23.5554550, 114.8868890);
    }
}
