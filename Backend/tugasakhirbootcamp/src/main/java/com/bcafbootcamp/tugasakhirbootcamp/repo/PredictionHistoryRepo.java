package com.bcafbootcamp.tugasakhirbootcamp.repo;

import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionHistoryRepo extends JpaRepository<PredictionHistory, Long> {
    List<PredictionHistory> findTop30ByOrderByIdDesc();
}
