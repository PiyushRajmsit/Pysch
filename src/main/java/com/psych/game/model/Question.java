package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.psych.game.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question extends Auditable{

    @Getter
    @Setter
    @NotBlank
    @Column(length = Constants.MAX_QUESTION_LENGTH)
    private String questionText;

    @Getter
    @Setter
    @NotBlank
    @Column(length = Constants.MAX_ANSWER_LENGTH)
    private String correctAnswer;

    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "question")
    @Getter
    @Setter
    @JsonManagedReference
    private List<EllenAnswer> ellenAnswers = new ArrayList<>();

    public Question(){}

    private Question(Builder builder) {
        this.questionText = builder.questionText;
        this.correctAnswer = builder.correctAnswer;
        this.gameMode = builder.gameMode;
    }

    public static Builder newQuestion() {
        return new Builder();
    }

    public static final class Builder {
        private @NotBlank String questionText;
        private @NotBlank String correctAnswer;
        private @NotNull GameMode gameMode;

        public Builder() {
        }

        public Question build() {
            return new Question(this);
        }

        public Builder questionText(String questionText) {
            this.questionText = questionText;
            return this;
        }

        public Builder correctAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public Builder gameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }
    }
}
