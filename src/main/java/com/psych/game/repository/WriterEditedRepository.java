package com.psych.game.repository;

import com.psych.game.model.ContentWriter;
import com.psych.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterEditedRepository extends JpaRepository<ContentWriter, Question> {
}
