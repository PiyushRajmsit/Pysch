package com.psych.game.controller;


import com.psych.game.model.Question;
import com.psych.game.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dev")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/questions")
    private List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    @GetMapping("/questions/{id}")
    private List<Question> getAllPlayers(@PathVariable(value = "id")Long id){
        return questionRepository.findAll();
    }



}
