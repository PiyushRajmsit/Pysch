package com.psych.game.repository;

import com.psych.game.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stats,Long> {
}
