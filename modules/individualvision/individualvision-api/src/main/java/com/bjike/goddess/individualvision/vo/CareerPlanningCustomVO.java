package com.bjike.goddess.individualvision.vo;

import com.bjike.goddess.common.api.type.Status;

/**
 * 职业规划定制表现层对象
 *
 * @Author: [ xiazhili ]
 * @Date: [ 2017-03-30 06:45 ]
 * @Description: [ 职业规划定制表现层对象 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
public class CareerPlanningCustomVO {

    /**
     * id
     */
    private String id;
    /**
     * 姓名
     */
    private String name;

    /**
     * 地区
     */
    private String area;

    /**
     * 部门
     */
    private String department;

    /**
     * 入职时间
     */
    private String entryTime;
    /**
     * 可提供的福利待遇
     */
    private String availableBenefitPackage;
    /**
     * 薪资上升幅度
     */
    private String expectedSalaryIncrease;

    /**
     * 规划日期
     */
    private String planningDate;

    /**
     * 目前状态
     */
    private String currentState;

    /**
     * 选择发展路径：
     */
    private String positive;


    /**
     * 此路径能力提升程度的选择
     */
    private String degreeAbilityAscend;

    /**
     * 此时的权利及义务
     */
    private String rightsObligations;

    /**
     * 预计完成时间
     */
    private String expectedCompletionTime;

    /**
     * 实际完成时间
     */
    private String actualCompletionTime;

    /**
     * 是否按时完成
     */
    private Boolean finish;

    /**
     * 期望周收益（元）
     */
    private String expectWeeksEarnings;

    /**
     * 期望月收益（元）
     */
    private String expectMonthsEarnings;

    /**
     * 期望年收益（元）
     */
    private String expectYearsEarnings;

    /**
     * 实际周收益（元）
     */
    private String actualWeeksEarnings;

    /**
     * 实际月收益（元）
     */
    private String actualMonthsEarnings;

    /**
     * 实际年收益（元）
     */
    private String actualYearsEarnings;

    public String getAvailableBenefitPackage() {
        return availableBenefitPackage;
    }

    public void setAvailableBenefitPackage(String availableBenefitPackage) {
        this.availableBenefitPackage = availableBenefitPackage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExpectedSalaryIncrease() {
        return expectedSalaryIncrease;
    }

    public void setExpectedSalaryIncrease(String expectedSalaryIncrease) {
        this.expectedSalaryIncrease = expectedSalaryIncrease;
    }

    public String getPlanningDate() {
        return planningDate;
    }

    public void setPlanningDate(String planningDate) {
        this.planningDate = planningDate;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }


    public String getDegreeAbilityAscend() {
        return degreeAbilityAscend;
    }

    public void setDegreeAbilityAscend(String degreeAbilityAscend) {
        this.degreeAbilityAscend = degreeAbilityAscend;
    }

    public String getRightsObligations() {
        return rightsObligations;
    }

    public void setRightsObligations(String rightsObligations) {
        this.rightsObligations = rightsObligations;
    }

    public String getExpectedCompletionTime() {
        return expectedCompletionTime;
    }

    public void setExpectedCompletionTime(String expectedCompletionTime) {
        this.expectedCompletionTime = expectedCompletionTime;
    }

    public String getActualCompletionTime() {
        return actualCompletionTime;
    }

    public void setActualCompletionTime(String actualCompletionTime) {
        this.actualCompletionTime = actualCompletionTime;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getExpectWeeksEarnings() {
        return expectWeeksEarnings;
    }

    public void setExpectWeeksEarnings(String expectWeeksEarnings) {
        this.expectWeeksEarnings = expectWeeksEarnings;
    }

    public String getExpectMonthsEarnings() {
        return expectMonthsEarnings;
    }

    public void setExpectMonthsEarnings(String expectMonthsEarnings) {
        this.expectMonthsEarnings = expectMonthsEarnings;
    }

    public String getExpectYearsEarnings() {
        return expectYearsEarnings;
    }

    public void setExpectYearsEarnings(String expectYearsEarnings) {
        this.expectYearsEarnings = expectYearsEarnings;
    }

    public String getActualWeeksEarnings() {
        return actualWeeksEarnings;
    }

    public void setActualWeeksEarnings(String actualWeeksEarnings) {
        this.actualWeeksEarnings = actualWeeksEarnings;
    }

    public String getActualMonthsEarnings() {
        return actualMonthsEarnings;
    }

    public void setActualMonthsEarnings(String actualMonthsEarnings) {
        this.actualMonthsEarnings = actualMonthsEarnings;
    }

    public String getActualYearsEarnings() {
        return actualYearsEarnings;
    }

    public void setActualYearsEarnings(String actualYearsEarnings) {
        this.actualYearsEarnings = actualYearsEarnings;
    }
}