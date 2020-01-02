package com.entity;

import java.math.BigDecimal;

/**快递站信息
 * @Author: pfsun
 * @Date: 2020-01-01 21:31
 */
public class ExpressS {

    private int id;
    private double jingdu;
    private double weidu;
    private double firstPrice;
    private double continuePrice;
    private int scale;//规模系数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getJingdu() {
        return jingdu;
    }

    public void setJingdu(double jingdu) {
        this.jingdu = jingdu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }

    public double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public double getContinuePrice() {
        return continuePrice;
    }

    public void setContinuePrice(double continuePrice) {
        this.continuePrice = continuePrice;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
