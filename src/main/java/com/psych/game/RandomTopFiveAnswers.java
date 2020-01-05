package com.psych.game;

import com.psych.game.model.EllenAnswer;
import com.psych.game.model.Round;

public class RandomTopFiveAnswers implements EllenStrategy{
    @Override
    public EllenAnswer getAnswer(Round round) {
        // make a db query
        // ellen answers and ques repository
        // find tp 5 answers
        // return one of them
        return new EllenAnswer();
    }
}
