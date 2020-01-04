package com.psych.game.controller;

import com.psych.game.model.Game;
import com.psych.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @GetMapping("/games")
    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    @GetMapping("/games/{id}")
    public Game getGameById(@PathVariable(value = "id")Long id) throws Exception{
        return gameRepository.findById(id).orElseThrow(Exception::new);
    }

}
