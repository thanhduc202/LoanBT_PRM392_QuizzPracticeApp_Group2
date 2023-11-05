package com.example.prm392_quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_quizapp.data.Quiz;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.UserDatabaseClient;
import com.example.prm392_quizapp.other.Constants;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView Title, tvQuestion, tvTotal;

    private Button btnNext;

    private RadioGroup radioGroup;

    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    List<Quiz> quizList;
    int currentQuiz = 0;
    int correct = 0;

    Subject subject;
    private Quiz quiz;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizList = new ArrayList<>();
        currentQuiz = 0;
        correct = 0;
        Intent intent = getIntent();
        if (intent != null) {
            subject = (Subject) intent.getParcelableExtra(Constants.SUBJECT);
            findViewById(R.id.imageViewStartQuiz).setOnClickListener(view -> finish());

            new AsyncTask<Void, Void, List<Quiz>>() {
                @Override
                protected List<Quiz> doInBackground(Void... voids) {

                    return UserDatabaseClient.getInstance(QuizActivity.this).quizDao().getQuizBySubject(subject.getSubjectID());
                }

                @Override
                protected void onPostExecute(List<Quiz> quizzes) {
                    quizList = quizzes;
                    Title = (TextView) findViewById(R.id.tv_quiz_subject);
                    tvQuestion = findViewById(R.id.tv_quiz_question);
                    radioGroup = findViewById(R.id.radioGroupQuiz);
                    radioButton1 = findViewById(R.id.radioButton1);
                    radioButton2 = findViewById(R.id.radioButton2);
                    radioButton3 = findViewById(R.id.radioButton3);
                    radioButton4 = findViewById(R.id.radioButton4);
                    btnNext = findViewById(R.id.btnNextQuestion);
                    tvTotal = findViewById(R.id.current_quiz);

                    Title.setText(subject.getSubjectName());

                    btnNext.setOnClickListener(view -> {

                        RadioButton radioButton =  findViewById(radioGroup.getCheckedRadioButtonId());
                        if(radioButton == null){
                            runOnUiThread(() -> Toast.makeText(QuizActivity.this, "Select Answer!!", Toast.LENGTH_SHORT).show());
                            return;
                        }
                        boolean answer = quiz.getCorrectAnswer().equals(radioButton.getText().toString());
                        if (answer){
                            correct++;
                        }

                        if (btnNext.getText().equals(getString(R.string.next))){
                            setData();
                        }else{
                            Intent intentResult = new Intent(QuizActivity.this,FinalResultActivity.class);
                            intentResult.putExtra(Constants.SUBJECT,subject);
                            intentResult.putExtra(Constants.CORRECT,correct);
                            intentResult.putExtra(Constants.INCORRECT,quizList.size() - correct);
                            intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentResult);
                            finish();
                        }
                    });

                    if(quizList.size()==0){
                        finish();
                    }else{
                        setData();
                    }
                }
            }.execute();


        }else {
            finish();
        }

    }

    private void setData() {
            quiz = quizList.get(currentQuiz);
            tvQuestion.setText(quiz.getQuestion());
            radioButton1.setText(quiz.getAnswer1());
            radioButton2.setText(quiz.getAnswer2());
            radioButton3.setText(quiz.getAnswer3());
            radioButton4.setText(quiz.getAnswer4());
            radioGroup.clearCheck();
            tvTotal.setText("Current Quiz: "+ (++currentQuiz));
        if (currentQuiz == Constants.QUESTION_SHOWING  - 1 || currentQuiz >= quizList.size()){
            btnNext.setText(getText(R.string.finish));
        }
    }

}