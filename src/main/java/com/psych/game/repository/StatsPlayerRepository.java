package com.psych.game.repository;
import com.psych.game.model.Player;
import com.psych.game.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsPlayerRepository extends JpaRepository<Stats,Player> {
}
