package com.bjike.goddess.bankrecords.vo;

import java.time.LocalDate;

/**
* 银行流水表现层对象
* @Author:			[ yangruizhen ]
* @Date:			[  2018-04-08 10:27 ]
* @Description:	[ 银行流水表现层对象 ]
* @Version:		[ v1.0.0 ]
* @Copy:   		[ com.bjike ]
*/
public class BankSummaryVO {
 /**
  * 账户id
  */
 private String accountId;
 /**
  * 银行名称
  */

 private String bankName;

 /**
  * 日期；Long
  */

 private String theDateOf;

 /**
  * 本月发生额
  */


 private Double amountOfThisMonth;

 /**
  * 收入贷方金额
  */
 private Double incomeCreditAmount;

 /**
  * 支出借方金额
  */

 private Double expenseDebitAmount;

 /**
  * 余额
  */

 private Double theBalanceOf;

 public String getAccountId() {
  return accountId;
 }

 public void setAccountId(String accountId) {
  this.accountId = accountId;
 }

 public void setAmountOfThisMonth(Double amountOfThisMonth) {
  this.amountOfThisMonth = amountOfThisMonth;
 }

 public Double getIncomeCreditAmount() {
  return incomeCreditAmount;
 }

 public void setIncomeCreditAmount(Double incomeCreditAmount) {
  this.incomeCreditAmount = incomeCreditAmount;
 }

 public Double getExpenseDebitAmount() {
  return expenseDebitAmount;
 }

 public void setExpenseDebitAmount(Double expenseDebitAmount) {
  this.expenseDebitAmount = expenseDebitAmount;
 }

 public Double getTheBalanceOf() {
  return theBalanceOf;
 }

 public void setTheBalanceOf(Double theBalanceOf) {
  this.theBalanceOf = theBalanceOf;
 }

 public String getBankName() {
  return bankName;
 }

 public void setBankName(String bankName) {
  this.bankName = bankName;
 }

 public String getTheDateOf() {
  return theDateOf;
 }

 public void setTheDateOf(String theDateOf) {
  this.theDateOf = theDateOf;
 }

 public Double getAmountOfThisMonth() {
  return amountOfThisMonth;
 }
}