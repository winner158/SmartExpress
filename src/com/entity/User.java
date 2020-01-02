package com.entity;

import java.math.BigDecimal;

/**用户信息
 * @Author: pfsun
 * @Date: 2020-01-01 21:31
 */
public class User {

    private int id;
    private double jingdu;
    private double weidu;
    private double weight;
    private double lengthP;
    private double widthP;
    private double heightP;
    private double unitCost;

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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLengthP() {
        return lengthP;
    }

    public void setLengthP(double lengthP) {
        this.lengthP = lengthP;
    }

    public double getWidthP() {
        return widthP;
    }

    public void setWidthP(double widthP) {
        this.widthP = widthP;
    }

    public double getHeightP() {
        return heightP;
    }

    public void setHeightP(double heightP) {
        this.heightP = heightP;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }
}
