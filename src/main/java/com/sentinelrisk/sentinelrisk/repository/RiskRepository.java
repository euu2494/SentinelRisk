package com.sentinelrisk.sentinelrisk.repository;

import com.sentinelrisk.sentinelrisk.entity.Priority;
import com.sentinelrisk.sentinelrisk.entity.Status;
import com.sentinelrisk.sentinelrisk.entity.Risk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
  List<Risk> findByPriority(Priority priority);
  List<Risk> findByStatus(Status status);
  List<Risk> findByPriorityAndStatus(Priority priority, Status status);
}
