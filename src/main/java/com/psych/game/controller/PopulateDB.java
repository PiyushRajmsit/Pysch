package com.psych.game.controller;

import com.psych.game.Constants;
import com.psych.game.Utils;
import com.psych.game.model.GameMode;
import com.psych.game.model.Player;
import com.psych.game.model.Question;
import com.psych.game.repository.PlayerRepository;
import com.psych.game.repository.QuestionRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/dev")
public class PopulateDB {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/add-questions-from-file")
    private void getAllQuestions() throws IOException {
        questionRepository.deleteAll();
        int questionNumber = 0;
        for(Map.Entry<String,GameMode> entry: Constants.QA_FILES.entrySet()) {
            String fileName = entry.getKey();
            GameMode gameMode = entry.getValue();
            for (Pair<String, String> question_answer : Utils.readQAFile(fileName)) {

                Question q = new Question();
                q.setQuestionText(question_answer.getKey());
                q.setCorrectAnswer(question_answer.getValue());
                q.setGameMode(gameMode);
                questionRepository.save(q);
                questionNumber++;
                if (questionNumber > Constants.MAX_QUESTION_TO_READ) {
                    break;
                }
            }
        }
    }

    @GetMapping("/add-dummy-players")
    private void addDummyPlayers() throws IOException {
        playerRepository.deleteAll();
        Player bonnie = new Player();
        bonnie.setName("Mrs.Bonnie");
        Player clyde = new Player();
        clyde.setName("Mr.Clyde");

        playerRepository.save(bonnie);
        playerRepository.save(clyde);
    }


}
