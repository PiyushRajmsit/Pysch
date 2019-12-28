package com.psych.game.controller;

import com.psych.game.model.ContentWriter;
import com.psych.game.model.Question;
import com.psych.game.repository.ContentWriterRepository;
import com.psych.game.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dev")
public class ContentWriterController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ContentWriterRepository ContentWriterRepository;

    @GetMapping("/content_writer")
    public List<ContentWriter> getAllContentWriter(){
        return ContentWriterRepository.findAll();
    }
    @GetMapping("/content_writer/{id}")
    public ContentWriter getContentWriterById(@PathVariable(value = "id") Long id) throws Exception{
        return ContentWriterRepository.findById(id).orElseThrow(Exception::new);
    }

    //@PutMapping("/content_writer/{writer_id}/question/{question_id}")
    @PutMapping("/content_writer/{id}")
    public ResponseEntity<Object> editQuestion(@PathVariable(value = "id")Long id, @Valid @RequestBody Question question) throws Exception{

        Question q = questionRepository.findById(question.getId()).orElseThrow(Exception::new);
        q.setQuestionText(question.getQuestionText());
        q.setCorrectAnswer(question.getCorrectAnswer());

        ContentWriter cw = ContentWriterRepository.findById(id).orElseThrow(Exception::new);
        List<Question> questionList = cw.getEditedQuestions();
        questionList.add(question);
        cw.setEditedQuestions(questionList);
        ContentWriterRepository.save(cw);

        return ResponseEntity.ok().build();
    }

}
