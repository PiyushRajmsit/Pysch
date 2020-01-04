package com.psych.game.controller;


import com.psych.game.Utils;
import com.psych.game.model.*;
import com.psych.game.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    StatsPlayerRepository statsPlayerRepository;
    @Autowired
    PlayerAnswerRepository playerAnswerRepository;

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

    @GetMapping("/join-game/{pid}/{gc}")
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

    // todo: Start Game -> pid , gid = Done
    // check if pid is actually the leader of the game
    // check if game has started
    // check game has not already been started
    // check game has more than 1 player

    @GetMapping("/start/{pid}/{gid}")
    public Game startGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "gid")Long gameId)
            throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);

        if (!game.getGameStatus().equals(GameStatus.JOINING)) {
                // throw error
        }
        else if (!player.equals(game.getLeader())) {
                // throw error
        }
        else if (game.getPlayers().size() <= 1) {
            // throw error
        }

        // We are now ready to play
        game.setGameStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }


    // todo : Endgame - pid,gid = Done
    // check to make sure only leader can end game
    @PutMapping("/end/{pid}/{gid")
    public Game endGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "gid") Long gameId) throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);

        if(!game.getLeader().equals(player)) {
            // throw error
        }
        game.setGameStatus(GameStatus.OVER);
        gameRepository.save(game);
        return game;
    }


    // todo: GetgameState  - gid => Partial Done
    // json - current round, stats of each player
    // current round state - submitting answer , selecting answer - round - over

    @GetMapping("/game-state")
    public String getGameState(@PathVariable(value = "id")Long gameId)throws Exception{
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Round round = game.getRounds().get(game.getCurrentRound());
        return game.getCurrentRound() + "-" + game.getPlayerStats() ;
    }




    // todo: Submit Answer - pid,gid,answer = Done
    // thinks of the checks
    @PutMapping("/submit-answer/{pid}/{gid}/{answer}")
    public String submitAnswer(@PathVariable(value = "pid")Long playerId, @PathVariable(value = "gid")Long gameId,
                               @PathVariable(value = "answer")String answer)
            throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        if(answer.length() == 0) {
            // throw error
        }

        PlayerAnswer playerAnswer = new PlayerAnswer();
        playerAnswer.setAnswer(answer);
        playerAnswer.setPlayer(player);
        playerAnswer.setRound(game.getRounds().get(game.getCurrentRound()));

        return "Success";
    }


    //todo: leave Game => Done
    //update the player Stats
    @PostMapping("/leave-game/{pid}/{gid}")
    public Game leaveGame(@PathVariable(value = "pid")Long playerId,@PathVariable(value = "pid")Long gameId)
            throws Exception{
        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Stats current_stats = game.getPlayerStats().get(player);
        Stats global_stats = statsPlayerRepository.findById(player).orElseThrow(Exception::new);

        Stats new_stats = new Stats();
        new_stats.setCorrectAnswers(current_stats.getCorrectAnswers()+global_stats.getCorrectAnswers());
        new_stats.setGotPsychedCount(current_stats.getGotPsychedCount()+global_stats.getGotPsychedCount());
        new_stats.setPsychedOthersCount(current_stats.getPsychedOthersCount()+new_stats.getPsychedOthersCount());
        statsPlayerRepository.save(new_stats);


        return game;
    }

    // todo:selectAnswer - pid,gid,answer-id =>Done
    // check if the anwser is correct
    // update the game and game stats
    // to detect if the game has ended,and to end game
    // when the game ends, update every player stats

    @PutMapping("/select-answer/{pid}/{gid}/{qid}")
    public Game selectAnswer(@PathVariable(value = "pid")Long playerId,
                             @PathVariable(value = "gid")Long gameId,
                             @PathVariable(value = "qid")Long questionId) throws Exception{

        Player player = playerRepository.findById(playerId).orElseThrow(Exception::new);
        Game game = gameRepository.findById(gameId).orElseThrow(Exception::new);
        Question question = questionRepository.findById(questionId).orElseThrow(Exception::new);
        Round round = game.getRounds().get(game.getCurrentRound());
        PlayerAnswer playerAnswer = round.getPlayerAnswers().get(player);

        if(game.getGameStatus().equals(GameStatus.OVER))
        {
            //throw error
        }
        if(question.getCorrectAnswer() != playerAnswer.getAnswer())
        {
            Stats current_stats = game.getPlayerStats().get(player);
            current_stats.setCorrectAnswers(current_stats.getCorrectAnswers()+1L);
        }


        return game;
    }

    //todo :getReady - pid,gid

}
