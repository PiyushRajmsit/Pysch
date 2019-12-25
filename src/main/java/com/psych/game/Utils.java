package com.psych.game;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {


    public static List<Pair<String,String>> readQAFile(String filename) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String question,answer;
        List<Pair<String,String>> question_answers = new ArrayList<>();
        try {
            do {
                question = br.readLine();
                answer = br.readLine();

                if(question == null || answer == null || question.length() > Constants.MAX_QUESTION_LENGTH - 1 || answer.length() > Constants.MAX_ANSWER_LENGTH - 1){
                    System.out.println("Skipping Question :" + question);
                    System.out.println("Skipping Answer :" + answer);
                }

                question_answers.add(new Pair<String, String>(question,answer));

            } while (question != null && answer != null);
        }catch (IOException ignored){
        }
        return question_answers;
    }
}
