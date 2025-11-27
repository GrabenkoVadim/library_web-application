package com.skilloVilla.Repository;

import com.skilloVilla.Entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
}
