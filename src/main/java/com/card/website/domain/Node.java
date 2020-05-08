package com.card.website.domain;

import javax.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name="p_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="r_id")
    private Regime regime;

    private String nodeNick;

    private String childId;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Regime getRegime() {
        return regime;
    }

    public void setRegime(Regime regime) {
        this.regime = regime;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent p) {
        this.parent = p;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getNodeNick() {
        return nodeNick;
    }

    public void setNodeNick(String nodeNick) {
        this.nodeNick = nodeNick;
    }

}