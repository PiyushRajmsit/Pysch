package com.psych.game;

import com.psych.game.model.Question;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utils {


    private static List<String> wordsList;
    private static Map<String,Integer> wordsIndices;

    static {

        wordsList = new ArrayList<>();
        wordsIndices = new HashMap<>();
        try {

            BufferedReader br = new BufferedReader(new FileReader("words.txt"));
            String word;

            do {
                word = br.readLine();
                wordsList.add(word);
                wordsIndices.put(word,wordsList.size()-1);
            } while (word != null);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



    public static List<Pair<String,String>> readQAFile(String filename) throws FileNotFoundException {

        String question,answer;
        List<Pair<String,String>> question_answers = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            do {
                question = br.readLine();
                answer = br.readLine();

                if(question == null || answer == null || question.length() > Constants.MAX_QUESTION_LENGTH - 1 ||
                        answer.length() > Constants.MAX_ANSWER_LENGTH - 1){
                    System.out.println("Skipping Question :" + question);
                    System.out.println("Skipping Answer :" + answer);
                }

                question_answers.add(new Pair<String, String>(question,answer));

            } while (question != null && answer != null);
        }catch (IOException ignored){
        }
        return question_answers;
    }

    public static String getSecretCodeFromId(Long id) {

        int base = wordsList.size();
        String code = "";
        while(id > 0){
            code += wordsList.get( (int)(id%base))+" ";
            id /= base;
        }

        return code;
        // todo: Convert id to code
    }

    public static long getGameIdFromSecretCode(String code){

        List<String> words = Arrays.asList(code.split(" "));
        Long gameId = 0L;
        int base = wordsList.size();
        for (String word : words){

            int index = wordsIndices.get(word);
            gameId = gameId*base + index;
        }

        return gameId;
    }

    public static Question getRandomQuestion() {


    }
}
