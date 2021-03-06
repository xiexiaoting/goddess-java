package com.bjike.goddess.checkfunds.vo;

import com.bjike.goddess.checkfunds.beanlist.Detail;

import java.util.List;

/**
 * 借方差异
 *
 * @Author: [chenjunhao]
 * @Date: [2017-06-14 15:17]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.bjike]
 */
public class DebtorDifferVO {
    /**
     * 时间
     */
    private String recordDate;

    /**
     * 资金流水借方
     */
    private Double income;

    /**
     * 银行流水id
     */
    private String id;

    /**
     * 银行流水行记录字段
     */
    List<Detail> detailList;

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Detail> detailList) {
        this.detailList = detailList;
    }
}
