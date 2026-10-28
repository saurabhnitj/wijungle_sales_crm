package com.saurabh.crm.dashboard;

import com.saurabh.crm.lead.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final LeadRepository leads;
    public DashboardController(LeadRepository leads) { this.leads = leads; }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        var all = leads.findAll();
        long open = all.stream().filter(l -> l.getStatus() != LeadStatus.WON && l.getStatus() != LeadStatus.LOST).count();
        BigDecimal pipelineValue = all.stream()
            .filter(l -> l.getStatus() != LeadStatus.WON && l.getStatus() != LeadStatus.LOST)
            .map(Lead::getEstimatedValue).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal wonRevenue = all.stream().filter(l -> l.getStatus() == LeadStatus.WON)
            .map(Lead::getEstimatedValue).filter(v -> v != null).reduce(BigDecimal.ZERO, BigDecimal::add);
        long overdue = all.stream().filter(l -> l.getNextFollowUpDate() != null
            && l.getNextFollowUpDate().isBefore(LocalDate.now())
            && l.getStatus() != LeadStatus.WON && l.getStatus() != LeadStatus.LOST).count();
        Map<LeadStatus, Long> byStatus = new EnumMap<>(LeadStatus.class);
        for (LeadStatus status : LeadStatus.values()) byStatus.put(status, leads.countByStatus(status));
        return Map.of("totalLeads", all.size(), "openLeads", open, "pipelineValue", pipelineValue,
            "wonRevenue", wonRevenue, "overdueFollowUps", overdue, "leadsByStatus", byStatus);
    }
}
