package com.example.triviaquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaquiz.data.AnswerListAsynResponse;
import com.example.triviaquiz.data.QuestionBank;
import com.example.triviaquiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String MESSAGE_ID = "high score" ;
    private TextView questionTextView;
    private TextView counterTextView;
    private TextView scoreText;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private Button trueButton;
    private Button falseButton;
    private int indexCounter = 0;
    private int scoreCounter=0;
    private int prevHighScore = 0;
    private int high_score_val;
    private TextView highScoreView;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = new QuestionBank().getQuestions(new AnswerListAsynResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                Log.d("main", "onCreate: " + questionArrayList );

                questionTextView.setText(questionArrayList.get(indexCounter).getAnswer());
                counterTextView.setText(indexCounter + " / " + questionList.size());
                scoreText.setText(String.format("Current Score: %d",scoreCounter));
                prevHighScore = scoreCounter;
                display_highscore();



            }
        });

        questionTextView = findViewById(R.id.question_textview);
        counterTextView = findViewById(R.id.counter_text);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        scoreText = findViewById(R.id.score);
        highScoreView = findViewById(R.id.high_score);


        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_button:
                indexCounter = (indexCounter +1) % questionList.size();

                updateQuestionList();

                break;
            case R.id.prev_button:
                if (indexCounter>0){
                    indexCounter = (indexCounter -1) % questionList.size();
                    updateQuestionList();
                }

                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestionList();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestionList();
                break;

        }

    }

    private void updateQuestionList() {
        String question =questionList.get(indexCounter).getAnswer();
        questionTextView.setText(question);
        counterTextView.setText(indexCounter + " / " + questionList.size());
        scoreText.setText(String.format("Current Score:%d", + scoreCounter));

        display_highscore();
        prevHighScore = scoreCounter;
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkAnswer(boolean userChoice) {
        int toastId;
        boolean actualAnswer = questionList.get(indexCounter).isAnswerTrue();
        if (userChoice == actualAnswer){
            fadeView();
            toastId = R.string.correct;
            scoreCounter+=10;
        }else{
            shake_animation();
            toastId = R.string.incorrect;
            if(scoreCounter>0){
                scoreCounter -= 5;
            }

        }
        Toast.makeText(MainActivity.this,toastId,Toast.LENGTH_LONG)
            .show();

    }

    private void shake_animation(){
        Animation shakeAnimation = AnimationUtils.loadAnimation(MainActivity.this
        ,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shakeAnimation);
        shakeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void high_score(){
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if( scoreCounter > prevHighScore){
            editor.putInt("high score", scoreCounter);
        }
        editor.putInt("high score", Math.max(scoreCounter, prevHighScore));

        editor.apply();

    }

    @SuppressLint("DefaultLocale")
    private void display_highscore(){
        high_score();
        SharedPreferences get_high_score = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        high_score_val = get_high_score.getInt("high score", Integer.parseInt("0"));
        highScoreView.setText(String.format("HIGH SCORE = %d ", high_score_val));

    }
}


