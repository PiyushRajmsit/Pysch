package com.psych.game.controller;


import com.psych.game.model.Stats;
import com.psych.game.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsRepository statsRepository;

    @GetMapping("/players")
    public List<Stats> getAllStats(){
        return statsRepository.findAll();
    }


}
