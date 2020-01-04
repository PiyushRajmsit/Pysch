package com.psych.game.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "games")
public  class Game extends Auditable{

    @Getter
    @Setter
    @NotNull
    private int numRounds = 0;

    @Getter
    @Setter
    private boolean hasEllen = false;

    @Getter
    @Setter
    private int currentRound = 0;


    @ManyToMany
    @Getter
    @Setter
    private Map<Player,Stats> playerStats;


    @ManyToMany
    @Getter
    @Setter
    private List<Player> players;

    @NotNull
    @Getter
    @Setter
    private GameMode gameMode;

    @NotNull
    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.JOINING;

    @ManyToOne
    @NotNull
    @Getter
    @Setter
    private Player Leader;

    @OneToMany(mappedBy = "game")
    @Getter
    @Setter
    private List<Round> rounds;



}
