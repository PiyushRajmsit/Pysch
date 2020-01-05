package com.psych.game.controller;


import com.psych.game.Utils;
import com.psych.game.exceptions.IllegalGameException;
import com.psych.game.exceptions.InsufficientPlayersException;
import com.psych.game.exceptions.*;
import com.psych.game.model.*;
import com.psych.game.repository.*;
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
    @Autowired
    PlayerAnswerRepository playerAnswerRepository;

    @GetMapping("/create/{pid}/{gm}/{nr}/{ellen}")
    public String createGame(@PathVariable(value = "pid")Long playerId,
                             @PathVariable(value = "gm")int gameMode,
                             @PathVariable(value = "nr")int numRounds,
                             @PathVariable(value = "ellen")int hasEllen) throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = new Game.Builder().hasEllen(hasEllen == 1).numRounds(numRounds)
                .gameMode(GameMode.fromValue(gameMode)).Leader(player).build();


        gameRepository.save(game);
        return "Created Game : " + game.getId() + "- Code->" + Utils.getSecretCodeFromId(game.getId());
    }

    @GetMapping("/create/{pid}/{gc}")
    public String joinGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "gc")String
                           gameCode) throws Exception, InvalidActionForGameStateException {

        Game game = gameRepository.findById(Utils.getGameIdFromSecretCode(gameCode)).orElseThrow(Exception::new);
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        game.addPlayer(player);
        gameRepository.save(game);

        return "Successfully Joined Game";
    }

    //todo: Start Game -> pid , gid
    //check if pid is actually the leader of the game
    //check if game has started
    // check game has not already been started
    // check game has more than 1 player

    @GetMapping("/start/{pid}/{gid}")
    public String startGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "gid")Long
                            gameId) throws Exception, IllegalGameException ,InvalidActionForGameStateException,InsufficientPlayersException{

        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);

        if(!game.getLeader().equals(player)){
            throw new IllegalGameException("Player has joined no such Game");
        }

        if(game.getPlayers().size() < 2){
            throw new InsufficientPlayersException("Not Sufficient Player to start the Game");
        }
        game.start();
        gameRepository.save(game);
        return "Game has Started";
    }




    // todo: GetgameState  - gid
    // json - current round, stats of each player
    //- current round state - submitting answer , selecting answer - round - over

    // todo: Submit Answer - pid,gid,answer
    // thinks of the checks

    @GetMapping("/submit-answer/{pid}/{gid}/{answer}")
    public String submitAnswer(@PathVariable(value = "pid")Long playerId,
                               @PathVariable(value = "gid")Long gameId,
                               @PathVariable(value = "answer") String answer
                               ) throws  Exception,InvalidActionForGameStateException,IllegalGameException {

        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);

        if(!game.hasPlayer(player)) {
            throw new IllegalGameException("Player has not joined yet");
        }
        game.submitAnswer(player,answer);
        gameRepository.save(game);

        return "Successfully Submited Answer";
    }



    //todo: leave Game
    //update the player Stats



    // todo:selectAnswer - pid,gid,answer-id
    // check if the anwser is correct
    // update the game and game stats
    // to detect if the game has ended,and to end game
    // when the game ends, update every player stats

    @GetMapping("/select-answer/{pid}/{gid}/{paid}")
    public String selectAnswer(@PathVariable(value = "pid")Long playerId,
                               @PathVariable(value = "gid")Long gameId,
                               @PathVariable(value = "paid") Long playerAnswerId)
            throws Exception, InvalidActionForGameStateException, IllegalGameException, InvalidInputException {

        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        PlayerAnswer playerAnswer = playerAnswerRepository.findById(playerAnswerId).orElseThrow(Exception::new);
        if(!game.hasPlayer(player)) {
            throw new IllegalGameException("Player has not joined yet");
        }

        game.selectAnswer(player,playerAnswer);
        gameRepository.save(game);
        return "Successfully Submited Answer";
    }


    @GetMapping("/get-ready/{pid}/{gid}")
    public String getReady(@PathVariable(value = "pid")Long playerId,
                               @PathVariable(value = "gid")Long gameId)
            throws Exception, InvalidActionForGameStateException, IllegalGameException, InvalidInputException {

        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);

        if(!game.hasPlayer(player)) {
            throw new IllegalGameException("Player has not joined yet");
        }

        game.getReady(player);
        game.addPlayer(player);
        gameRepository.save(game);

        return "Successfully Submited Answer";
    }

    @GetMapping("/get-ready/{gid}")
    public String getGameState(@PathVariable(value = "gid")Long gameId) throws Exception{
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        return game.getState();
    }



}
