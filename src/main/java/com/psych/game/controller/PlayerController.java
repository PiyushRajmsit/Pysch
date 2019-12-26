package com.psych.game.controller;


import com.psych.game.model.Player;
import com.psych.game.repository.PlayerRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid   ;
import java.util.List;

@RestController
@RequestMapping("/dev")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/players")
    private List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("/players/{id}")
    private Player getAllPlayers(@PathVariable(value = "id")Long id) throws Exception{
        return playerRepository.findById(id).orElseThrow(Exception::new);
    }



}
