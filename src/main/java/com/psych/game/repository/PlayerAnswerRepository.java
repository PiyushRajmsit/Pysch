package com.psych.game.repository;

import com.psych.game.model.Player;
import com.psych.game.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerAnswerRepository extends JpaRepository<Player, PlayerAnswer> {
}
