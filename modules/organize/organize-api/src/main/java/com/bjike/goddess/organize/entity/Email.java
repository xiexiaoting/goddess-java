package com.bjike.goddess.organize.entity;

import com.bjike.goddess.common.api.entity.BaseEntity;
import com.bjike.goddess.organize.enums.IntervalType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by ike on 17-9-7.
 */
@Entity
@Table(name = "organize_email")
public class Email extends BaseEntity {
    /**
     * 项目组/部门
     */
    @Column(columnDefinition = "TEXT COMMENT '项目组/部门'", nullable = false)
    private String depart;
    /**
     * 是否发送至本项目组所有人
     */
    @Column(columnDefinition = "TINYINT(1)  COMMENT '是否发送至本项目组所有人'", nullable = false)
    private Boolean all;
    /**
     * 间隔类型
     */
    @Column(columnDefinition = "TINYINT(2)  COMMENT '间隔类型'", nullable = false)
    private IntervalType intervalType;
    /**
     * 间隔时长
     */
    @Column(columnDefinition = "INT(11)  COMMENT '间隔时长'", nullable = false)
    private Integer interval;

    /**
     * 发送时间
     */
    @Column(columnDefinition = "DATETIME  COMMENT '发送时间'", nullable = false)
    private LocalDateTime setTime;

    /**
     * 上次发送时间
     */
    @Column(columnDefinition = "DATETIME  COMMENT '上次发送时间'", nullable = false)
    private LocalDateTime lastTime;
    /**
     * 发送对象
     */
    @Column(columnDefinition = "TEXT COMMENT '发送对象'")
    private String sendObject;

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public LocalDateTime getSetTime() {
        return setTime;
    }

    public void setSetTime(LocalDateTime setTime) {
        this.setTime = setTime;
    }

    public String getSendObject() {
        return sendObject;
    }

    public void setSendObject(String sendObject) {
        this.sendObject = sendObject;
    }
}
