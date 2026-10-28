package com.saurabh.crm.lead;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads", indexes = {
    @Index(name = "idx_lead_status", columnList = "status"),
    @Index(name = "idx_lead_company", columnList = "companyName")
})
public class Lead {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String contactName;

    @Column(nullable = false, length = 160)
    private String companyName;

    @Column(length = 160)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 80)
    private String industry;

    @Column(length = 80)
    private String leadSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LeadStatus status = LeadStatus.NEW;

    @Column(precision = 14, scale = 2)
    private BigDecimal estimatedValue = BigDecimal.ZERO;

    @Column(length = 120)
    private String assignedTo;

    private LocalDate nextFollowUpDate;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) status = LeadStatus.NEW;
        if (estimatedValue == null) estimatedValue = BigDecimal.ZERO;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getLeadSource() { return leadSource; }
    public void setLeadSource(String leadSource) { this.leadSource = leadSource; }
    public LeadStatus getStatus() { return status; }
    public void setStatus(LeadStatus status) { this.status = status; }
    public BigDecimal getEstimatedValue() { return estimatedValue; }
    public void setEstimatedValue(BigDecimal estimatedValue) { this.estimatedValue = estimatedValue; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public LocalDate getNextFollowUpDate() { return nextFollowUpDate; }
    public void setNextFollowUpDate(LocalDate nextFollowUpDate) { this.nextFollowUpDate = nextFollowUpDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
