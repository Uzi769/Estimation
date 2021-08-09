package ru.irlix.evaluation.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="estimate")
public class Estimate {

    @Id
    private Long id;
    private String name;
    private Date create_date;
    private String description;
    private Integer risk;
    private Integer status;
    private String client;
    private String creator;

    @OneToMany(mappedBy = "estimate")
    private List<Phase> phase;

    public Estimate() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRisk(Integer risk) {
        this.risk = risk;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setClient(String client) { this.client = client; }

    public void setCreator(String creator) { this.creator = creator; }

    public void setPhase(List<Phase> phase) {
        this.phase = phase;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRisk() {
        return risk;
    }

    public Integer getStatus() {
        return status;
    }

    public String getClient() { return client; }

    public String getCreator() { return creator; }

    public List<Phase> getPhase() {
        return phase;
    }
}
