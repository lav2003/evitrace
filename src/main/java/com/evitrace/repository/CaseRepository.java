package com.evitrace.repository;

import com.evitrace.model.Case;
import com.evitrace.model.CaseStatus;
import com.evitrace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends JpaRepository<Case, Long> {

    Optional<Case> findByCaseNumber(String caseNumber);

    boolean existsByCaseNumber(String caseNumber);

    List<Case> findByCreatedBy(User user);

    List<Case> findByStatus(CaseStatus status);
}