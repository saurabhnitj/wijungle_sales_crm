package com.saurabh.crm.activity;

import com.saurabh.crm.lead.Lead;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_activities", indexes = @Index(name = "idx_activity_due", columnList = "nextActionAt"))
public class SalesActivity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityType type;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Column(length = 120)
    private String createdBy;

    private LocalDateTime happenedAt;
    private LocalDateTime nextActionAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        if (happenedAt == null) happenedAt = createdAt;
    }

    public Long getId() { return id; }
    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }
    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getHappenedAt() { return happenedAt; }
    public void setHappenedAt(LocalDateTime happenedAt) { this.happenedAt = happenedAt; }
    public LocalDateTime getNextActionAt() { return nextActionAt; }
    public void setNextActionAt(LocalDateTime nextActionAt) { this.nextActionAt = nextActionAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
