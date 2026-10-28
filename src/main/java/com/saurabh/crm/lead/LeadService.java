package com.saurabh.crm.lead;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class LeadService {
    private final LeadRepository repository;
    public LeadService(LeadRepository repository) { this.repository = repository; }

    public Page<Lead> list(LeadStatus status, String search, int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "page must be >= 0 and size must be 1..100");
        }
        Specification<Lead> spec = (root, query, cb) -> cb.conjunction();
        if (status != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        if (search != null && !search.isBlank()) {
            String pattern = "%" + search.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("companyName")), pattern),
                cb.like(cb.lower(root.get("contactName")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
            ));
        }
        return repository.findAll(spec, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Lead get(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found: " + id));
    }

    public Lead create(LeadRequest request) {
        Lead lead = new Lead();
        apply(lead, request);
        return repository.save(lead);
    }

    public Lead update(Long id, LeadRequest request) {
        Lead lead = get(id);
        apply(lead, request);
        return repository.save(lead);
    }

    public Lead updateStatus(Long id, LeadStatus status) {
        if (status == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        Lead lead = get(id);
        lead.setStatus(status);
        return repository.save(lead);
    }

    public void delete(Long id) {
        Lead lead = get(id);
        repository.delete(lead);
    }

    private void apply(Lead lead, LeadRequest r) {
        lead.setContactName(r.contactName());
        lead.setCompanyName(r.companyName());
        lead.setEmail(r.email());
        lead.setPhone(r.phone());
        lead.setIndustry(r.industry());
        lead.setLeadSource(r.leadSource());
        if (r.status() != null) lead.setStatus(r.status());
        if (r.estimatedValue() != null) lead.setEstimatedValue(r.estimatedValue());
        lead.setAssignedTo(r.assignedTo());
        lead.setNextFollowUpDate(r.nextFollowUpDate());
        lead.setNotes(r.notes());
    }
}
