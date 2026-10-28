package com.saurabh.crm.lead;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {
    private final LeadService service;
    public LeadController(LeadService service) { this.service = service; }

    @GetMapping
    public Page<Lead> list(
        @RequestParam(required = false) LeadStatus status,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        return service.list(status, search, page, size);
    }

    @GetMapping("/{id}")
    public Lead get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Lead create(@Valid @RequestBody LeadRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    public Lead update(@PathVariable Long id, @Valid @RequestBody LeadRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public Lead updateStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        return service.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    public record StatusRequest(LeadStatus status) {}
}
