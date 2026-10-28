package com.saurabh.crm.activity;

import com.saurabh.crm.lead.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/leads/{leadId}/activities")
public class SalesActivityController {
    private final SalesActivityRepository activities;
    private final LeadService leads;

    public SalesActivityController(SalesActivityRepository activities, LeadService leads) {
        this.activities = activities;
        this.leads = leads;
    }

    @GetMapping
    public List<SalesActivity> list(@PathVariable Long leadId) {
        leads.get(leadId);
        return activities.findByLeadIdOrderByHappenedAtDesc(leadId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalesActivity create(@PathVariable Long leadId, @Valid @RequestBody ActivityRequest request) {
        SalesActivity activity = new SalesActivity();
        activity.setLead(leads.get(leadId));
        activity.setType(request.type());
        activity.setSummary(request.summary());
        activity.setCreatedBy(request.createdBy());
        activity.setHappenedAt(request.happenedAt());
        activity.setNextActionAt(request.nextActionAt());
        return activities.save(activity);
    }
}
