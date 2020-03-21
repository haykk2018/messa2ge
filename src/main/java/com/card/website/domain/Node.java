package com.card.website.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nodeNick;

    @NotNull
    private String parentId;

    private String childId;


    @Column(name = "begin_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private Date beginDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    private Date endDate;

    private LocalTime startDailyTime;

    private LocalTime endDailyTime;

    private boolean opened;

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public LocalTime getEndDailyTime() {
        return endDailyTime;
    }

    public void setEndDailyTime(LocalTime endDailyTime) {
        this.endDailyTime = endDailyTime;
    }

    public LocalTime getStartDailyTime() {
        return startDailyTime;
    }

    public void setStartDailyTime(LocalTime dailyTime) {
        this.startDailyTime = dailyTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNodeNick() {
        return nodeNick;
    }

    public void setNodeNick(String nodeNick) {
        this.nodeNick = nodeNick;
    }

}