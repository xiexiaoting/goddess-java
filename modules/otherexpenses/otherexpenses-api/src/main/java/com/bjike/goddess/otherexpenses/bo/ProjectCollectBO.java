package com.bjike.goddess.otherexpenses.bo;

import java.io.Serializable;

/**
 * @Author: [dengjunren]
 * @Date: [2017-05-03 15:43]
 * @Description: [ 项目组汇总 ]
 * @Version: [1.0.0]
 * @Copy: [com.bjike]
 */
public class ProjectCollectBO implements Serializable {

    /**
     * 项目组
     */
    private String project;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 目标其他费用
     */
    private Double target;

    /**
     * 实际其他费用
     */
    private Double actual;

    /**
     * 比例
     */
    private Double ratio;

    /**
     * 差额
     */
    private Double balance;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
