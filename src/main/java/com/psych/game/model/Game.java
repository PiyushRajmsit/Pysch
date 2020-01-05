package com.psych.game.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.Utils;
import com.psych.game.exceptions.InvalidActionForGameStateException;
import com.psych.game.exceptions.InvalidInputException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Map<Player,Stats> playerStats = new HashMap<>();

    @ManyToMany
    @Getter
    @Setter
    @JsonIdentityReference
    private List<Player> players = new ArrayList<>();

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
    @JsonIdentityReference
    private Player Leader;

    @OneToMany(mappedBy = "game",cascade = CascadeType.ALL)
    @Getter
    @Setter
    @JsonManagedReference
    private List<Round> rounds = new ArrayList<>();


    public Game(){}


    public Game(Builder builder) {
        this.numRounds = builder.numRounds;
        this.hasEllen = builder.hasEllen;
        this.gameMode = builder.gameMode;
        this.Leader = builder.Leader;

        try {
            addPlayer(Leader);
        }
        catch (InvalidActionForGameStateException ignored){
        }
    }

    public boolean hasPlayer(Player player){
        return playerStats.containsKey(player);
    }


    public void addPlayer(Player player) throws InvalidActionForGameStateException {

        if(!gameStatus.equals(GameStatus.JOINING))
        {
            throw new InvalidActionForGameStateException("Cannot Join Game because it has already Started");
        }
        if(playerStats.containsKey(player))
            return;

        players.add(player);
        playerStats.put(player,new Stats());
    }


    public static Builder newGame() {
        return new Builder();
    }



    private void startNewRound(){
        Round round = new Round(this, Utils.getRandomQuestion(),1);
        rounds.add(round);
        gameStatus = GameStatus.SUBMITTING_ANSWER;
    }


    public void start() throws InvalidActionForGameStateException {
        if(!gameStatus.equals(GameStatus.JOINING))
        {
            throw new InvalidActionForGameStateException("Game has already Started");
        }
        startNewRound();
    }

    public void submitAnswer(Player player, String answer) throws InvalidActionForGameStateException{

        if(!gameStatus.equals(GameStatus.SUBMITTING_ANSWER)){
            throw new InvalidActionForGameStateException("Not Accepting Any Answers At The Moment");
        }
        Round round = currentRound();
        round.submitAnswer(player,answer);

        if(round.getSubmittedAnswers().size() == players.size())
        {
            gameStatus = GameStatus.SELECTING_ANSWER;
        }
        return;
    }

    public Round currentRound(){
        return rounds.get(rounds.size()-1);
    }


    public void selectAnswer(Player player, PlayerAnswer playerAnswer) throws InvalidActionForGameStateException, InvalidInputException {
        if(!gameStatus.equals(GameStatus.SELECTING_ANSWER)){
            throw new InvalidActionForGameStateException("Not Selecting Any Answers At The Moment");
        }
        Round round = currentRound();
        round.selectAnswer(player,playerAnswer);

        if(round.getSelectedAnswers().size() == players.size())
        {
            if(rounds.size() < numRounds)
                gameStatus = GameStatus.GETTING_READY;
            else {
                gameStatus = GameStatus.OVER;
                // Todo: Update Ellen
            }
        }
    }

    public void getReady(Player player) {

        Round round = currentRound();
        round.getReady(player);

        if(round.getReadyPlayers().size()== players.size())
            startNewRound();

    }

    public String getState() {

        return " ";
    }


    public static final class Builder {
        private @NotNull int numRounds;
        private boolean hasEllen;
        private @NotNull GameMode gameMode;
        private @NotNull Player Leader;

        public Builder() {
        }

        public Game build() {
            return new Game(this);
        }

        public Builder numRounds(int numRounds) {
            this.numRounds = numRounds;
            return this;
        }

        public Builder hasEllen(boolean hasEllen) {
            this.hasEllen = hasEllen;
            return this;
        }

        public Builder gameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder Leader(Player Leader) {
            this.Leader = Leader;
            return this;
        }
    }
}
