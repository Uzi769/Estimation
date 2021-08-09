package ru.irlix.evaluation.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;

@Entity
@Table(name="phase")
public class Phase {

    @Id
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "estimate", foreignKey = @ForeignKey(name="FK_ESTIMATE"))
    private Estimate estimate;

    private Integer sort_order;
    private Integer management_reserve;
    private Integer bags_reserve;
    private Integer qa_reserve;
    private Integer risk_reserve;
    private String estimate_role;

    public Phase() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

    public void setManagement_reserve(Integer management_reserve) {
        this.management_reserve = management_reserve;
    }

    public void setBags_reserve(Integer bags_reserve) {
        this.bags_reserve = bags_reserve;
    }

    public void setQa_reserve(Integer qa_reserve) {
        this.qa_reserve = qa_reserve;
    }

    public void setRisk_reserve(Integer risk_reserve) {
        this.risk_reserve = risk_reserve;
    }

    public void setEstimate_role(String estimate_role) {
        this.estimate_role = estimate_role;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public Integer getSort_order() {
        return sort_order;
    }

    public Integer getManagement_reserve() {
        return management_reserve;
    }

    public Integer getBags_reserve() {
        return bags_reserve;
    }

    public Integer getQa_reserve() {
        return qa_reserve;
    }

    public Integer getRisk_reserve() {
        return risk_reserve;
    }

    public String getEstimate_role() {
        return estimate_role;
    }

}
