package com.example.prm392_quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.prm392_quizapp.data.Attempt;
import com.example.prm392_quizapp.data.Quiz;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.User;
import com.example.prm392_quizapp.data.UserDatabaseClient;
import com.example.prm392_quizapp.other.Constants;
import com.example.prm392_quizapp.other.SharedPref;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Result;

public class FinalResultActivity extends AppCompatActivity {

    TextView tvWellDone, tvSubject, tvCorrect, tvIncorrect, tvEarned, tvOverall, tvDate;
    Button btnAgain;

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        Intent intent = getIntent();
        if (intent != null) {

            SharedPref sharedPref = SharedPref.getInstance();
            User user = sharedPref.getUser(this);
            Date date = new Date();
            Subject subject = (Subject)intent.getParcelableExtra(Constants.SUBJECT);
            int correct = intent.getIntExtra(Constants.CORRECT, 0);
            int incorrect = intent.getIntExtra(Constants.INCORRECT, 0);
            Attempt result = new Attempt(
                    date.getTime(),
                    subject.getSubjectName(),
                    correct,
                    incorrect,
                    correct*20,
                    user.getEmail()
            );
            result.setOverallPoints((correct+incorrect)*20);

            findViewById(R.id.imageViewFinalResultQuiz).setOnClickListener(view -> finish());


            tvWellDone = findViewById(R.id.tvWellDone);
            tvSubject = findViewById(R.id.textView16);
            tvCorrect = findViewById(R.id.textView19);
            tvIncorrect = findViewById(R.id.textView27);
            tvEarned = findViewById(R.id.textView28);
            tvOverall = findViewById(R.id.textView29);
            tvDate = findViewById(R.id.textView30);
            btnAgain = findViewById(R.id.btnFinishQuiz);

            String wellDone = "WELL DONE, " + user.getUsername();
            tvWellDone.setText(wellDone);
            tvSubject.setText(subject.getSubjectName());
            tvIncorrect.setText(String.valueOf(incorrect));
            tvCorrect.setText(String.valueOf(correct));
            tvEarned.setText(String.valueOf(correct*20));
            tvOverall.setText(String.valueOf((correct+incorrect)*20));
            tvDate.setText(date.toString());

            btnAgain.setOnClickListener(view -> {
                Intent intent1 = new Intent(this, QuizActivity.class);
                intent1.putExtra(Constants.SUBJECT, subject);
                startActivity(intent1);
                finish();
            });

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    UserDatabaseClient.getInstance(FinalResultActivity.this).userDao().saveAttempt(result);
                    return null;
                }
            }.execute();
        }else{
            finish();
        }
    }
}