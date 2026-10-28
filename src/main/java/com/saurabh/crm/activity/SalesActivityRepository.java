package com.saurabh.crm.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesActivityRepository extends JpaRepository<SalesActivity, Long> {
    List<SalesActivity> findByLeadIdOrderByHappenedAtDesc(Long leadId);
}
