package com.skilloVilla.Repository;

import com.skilloVilla.Entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    List<ActionLog> findTop50ByOrderByTimestampDesc();
}
