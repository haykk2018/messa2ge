package com.card.website.domain;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class

public class Regime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String regimeId;

    @NotNull
    private String parentId;

    @Size(min = 2, max = 30)
    private String regimeNick;

    private String regimeDescription;

    //when I dont put this annotation I have problem in server(from view times came null) but in local without annotation also every thing works normally
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startDailyTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endDailyTime;

    private boolean d1;
    private boolean d2;
    private boolean d3;
    private boolean d4;
    private boolean d5;
    private boolean d6;
    private boolean d7;


    @OneToMany(mappedBy= "regime", fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE})
    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public LocalTime getStartDailyTime() {
        return startDailyTime;
    }

    public void setStartDailyTime(LocalTime startDailyTime) {
        this.startDailyTime = startDailyTime;
    }

    public LocalTime getEndDailyTime() {
        return endDailyTime;
    }

    public void setEndDailyTime(LocalTime endDailyTime) {
        this.endDailyTime = endDailyTime;
    }


    public boolean isD1() {
        return d1;
    }

    public void setD1(boolean d1) {
        this.d1 = d1;
    }

    public boolean isD2() {
        return d2;
    }

    public void setD2(boolean d2) {
        this.d2 = d2;
    }

    public boolean isD3() {
        return d3;
    }

    public void setD3(boolean d3) {
        this.d3 = d3;
    }

    public boolean isD4() {
        return d4;
    }

    public void setD4(boolean d4) {
        this.d4 = d4;
    }

    public boolean isD5() {
        return d5;
    }

    public void setD5(boolean d5) {
        this.d5 = d5;
    }

    public boolean isD6() {
        return d6;
    }

    public void setD6(boolean d6) {
        this.d6 = d6;
    }

    public boolean isD7() {
        return d7;
    }

    public void setD7(boolean d7) {
        this.d7 = d7;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegimeId() {
        return regimeId;
    }

    public void setRegimeId(String parentId) {
        this.regimeId = parentId;
    }

    public String getRegimeNick() {
        return regimeNick;
    }

    public void setRegimeNick(String parentNick) {
        this.regimeNick = parentNick;
    }

    public String getRegimeDescription() {
        return regimeDescription;
    }

    public void setRegimeDescription(String parentDescription) {
        this.regimeDescription = parentDescription;
    }

}