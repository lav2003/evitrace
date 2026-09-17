package com.evitrace.repository;

import com.evitrace.model.ChainOfCustody;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChainOfCustodyRepository extends JpaRepository<ChainOfCustody, Long> {

    // Spring Data JPA reads the method name and generates the SQL automatically
    // SELECT * FROM chain_of_custody WHERE evidence_id = ? ORDER BY timestamp ASC
    List<ChainOfCustody> findByEvidenceIdOrderByTimestampAsc(Long evidenceId);
}