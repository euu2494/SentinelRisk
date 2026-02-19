package com.sentinelrisk.sentinelrisk.domain.risk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
  List<Risk> findByPriority(Priority priority);
  List<Risk> findByStatus(Status status);
  List<Risk> findByPriorityAndStatus(Priority priority, Status status);
}
