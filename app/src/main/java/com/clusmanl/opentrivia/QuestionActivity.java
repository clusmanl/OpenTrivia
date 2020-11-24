package com.clusmanl.opentrivia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clusmanl.opentrivia.util.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.*;

/**
 * @author Lucas Clusman
 * Contains code which gets questions and answers from OpenTrivia database.
 */
public class QuestionActivity extends AppCompatActivity {

    private final int MAX_QUESTIONS = 10;

    private Question[] questions;
    private int indexQuestion = 0;
    private Map<Integer, Integer> idsButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        questions = new Question[MAX_QUESTIONS];
        idsButtons = new HashMap<>(); // verifies the chosen answer with the button id
        idsButtons.put(1, R.id.answer1);
        idsButtons.put(2, R.id.answer2);
        idsButtons.put(3, R.id.answer3);
        idsButtons.put(4, R.id.answer4);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    try{
                        // Connection to the database
                        int category;
                        String urlText = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&type=multiple";
                        if((category = getIntent().getIntExtra("category", -1)) != -1){
                            urlText += "&category="+category;
                        }
                        URL url = new URL(urlText);
                        URLConnection connection = url.openConnection();

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String content = "";
                        String inputLine;
                        while((inputLine = bufferedReader.readLine()) != null){
                            content += inputLine;
                        }
                        bufferedReader.close();

                        JSONObject output = new JSONObject(content);
                        JSONArray results = output.getJSONArray("results");

                        Random rand = new Random();

                        for(int i = 0; i < 10; i++){
                            String question = (String) ((JSONObject) results.get(i)).get("question");
                            String[] answers = new String[4]; // first answer is the correct one
                            answers[0] = (String) ((JSONObject) results.get(i)).get("correct_answer");
                            JSONArray incorrect_answers = ((JSONObject) results.get(i)).getJSONArray("incorrect_answers");
                            for(int j = 0; j <  incorrect_answers.length(); j++){
                                answers[j+1] = incorrect_answers.getString(j).replaceAll("&quot;","").replaceAll("&#039;","'");
                            }

                            int correctAnswerId = rand.nextInt(4) + 1; //  we determine the correct answer button

                            questions[i] = new Question(question.replaceAll("&quot;","").replaceAll("&#039;","'"), answers, correctAnswerId);

                        }

                        changeQuestion();
                    } catch(MalformedURLException e){
                        e.printStackTrace();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    /**
     * Handles the correction of the user's answer
     * @param v
     */
    public void onClickAnswer(View v){

        int goodAnswerId = questions[indexQuestion].getCorrectAnswerId();

        if(idsButtons.get(goodAnswerId) == v.getId()){
            v.setBackgroundColor(Color.GREEN);
        } else {
            v.setBackgroundColor(Color.RED);
            Button goodAnswerButton = findViewById(idsButtons.get(goodAnswerId));
            goodAnswerButton.setBackgroundColor(Color.GREEN);
        }

        // We allow the user to see if he's correct
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                indexQuestion++;
                changeQuestion();
            }
        }, 3000);

    }

    public void changeQuestion(){

        Question questionObject = questions[indexQuestion];
        String question = questionObject.getQuestion();
        String[] answers = questionObject.getAnswers();

        TextView questionText = findViewById(R.id.questionId);
        questionText.setText(question);

        int goodAnswerId = questionObject.getCorrectAnswerId();

        Button b1 = findViewById(R.id.answer1);
        Button b2 = findViewById(R.id.answer2);
        Button b3 = findViewById(R.id.answer3);
        Button b4 = findViewById(R.id.answer4);
        b1.setBackgroundColor(Color.WHITE);
        b2.setBackgroundColor(Color.WHITE);
        b3.setBackgroundColor(Color.WHITE);
        b4.setBackgroundColor(Color.WHITE);

        switch(goodAnswerId){
            case 1:
                b1.setText(answers[0]);
                b2.setText(answers[1]);
                b3.setText(answers[2]);
                b4.setText(answers[3]);
                break;

            case 2:
                b1.setText(answers[1]);
                b2.setText(answers[0]);
                b3.setText(answers[2]);
                b4.setText(answers[3]);
                break;

            case 3:
                b1.setText(answers[2]);
                b2.setText(answers[1]);
                b3.setText(answers[0]);
                b4.setText(answers[3]);
                break;

            case 4:
                b1.setText(answers[3]);
                b2.setText(answers[1]);
                b3.setText(answers[2]);
                b4.setText(answers[0]);
                break;

            default:
                System.out.println("Error");
        }
    }
}
