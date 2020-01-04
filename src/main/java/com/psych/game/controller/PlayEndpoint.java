package com.psych.game.controller;


import com.psych.game.Utils;
import com.psych.game.model.Game;
import com.psych.game.model.GameMode;
import com.psych.game.model.GameStatus;
import com.psych.game.model.Player;
import com.psych.game.repository.GameRepository;
import com.psych.game.repository.PlayerRepository;
import com.psych.game.repository.QuestionRepository;
import com.psych.game.repository.RoundRepository;
import com.sun.org.apache.bcel.internal.generic.ExceptionThrower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play")
public class PlayEndpoint {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RoundRepository roundRepository;


    @GetMapping("/create/{pid}/{gm}/{nr}")
    public String createGame(@PathVariable(value = "pid")Long playerId,
                             @PathVariable(value = "gm")int gameMode,
                             @PathVariable(value = "nr")int numrounds) throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        GameMode mode = GameMode.IS_IT_A_FACT;
        Game game = new Game();
        game.setLeader(player);
        game.setNumRounds(numrounds);
        game.setGameMode(mode);
        gameRepository.save(game);
        game.getPlayers().add(player);
        gameRepository.save(game);
        return " " + game.getId() + "-" + Utils.getSecretCodeFromId(game.getId());
    }

    @GetMapping("/create/{pid}/{gc}")
    public String joinGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "gc")String
                           gameCode) throws Exception{

        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow(Exception::new);

        if(!game.getGameStatus().equals(GameStatus.JOINING)){
            //throw some error
        }

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        game.getPlayers().add(player);
        gameRepository.save(game);
        return "Successfully Joined";
    }

    //todo: Start Game -> pid , gid
    //check if pid is actually the leader of the game
    //check if game has started
    // check game has not already been started
    // check game has more than 1 player




    // todo: GetgameState  - gid
    // json - current round, stats of each player
    //- current round state - submitting answer , selecting answer - round - over

    // todo: Submit Answer - pid,gid,answer
    // thinks of the checks


    //todo: leave Game
    //update the player Stats



    // todo:selectAnswer - pid,gid,answer-id
    // check if the anwser is correct
    // update the game and game stats
    // to detect if the game has ended,and to end game
    // when the game ends, update every player stats


    //getReady - pid,gid





}
