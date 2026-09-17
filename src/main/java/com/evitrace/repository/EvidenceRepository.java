package com.evitrace.repository;

import com.evitrace.model.Case;
import com.evitrace.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByEvidenceCase(Case evidenceCase);
}