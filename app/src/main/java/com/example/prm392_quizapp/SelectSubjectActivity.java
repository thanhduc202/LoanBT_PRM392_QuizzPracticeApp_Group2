package com.example.prm392_quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_quizapp.adapter.SelectedSubjectAdapter;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.UserDatabase;
import com.example.prm392_quizapp.data.UserDatabaseClient;
import com.example.prm392_quizapp.other.Constants;

import java.util.ArrayList;

public class SelectSubjectActivity extends AppCompatActivity {

    private RecyclerView rvSelectedSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        rvSelectedSubject = findViewById(R.id.rv_selected_subject);

        findViewById(R.id.imageViewSelectQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        new GetAllSelectedSubjectTask().execute();
    }

    class GetAllSelectedSubjectTask extends AsyncTask<Void, Void, Void> {

        ArrayList<Subject> subjects = new ArrayList<>();

        public GetAllSelectedSubjectTask() {
        }


        @Override
        protected Void doInBackground(Void... voids) {
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            subjects = (ArrayList<Subject>) databaseClient.subjectDao().getAllSubjects();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            SelectedSubjectAdapter adapter = new SelectedSubjectAdapter(subjects, databaseClient.subjectDao());

            adapter.setOnClickListener(subject -> {
                Intent intent = new Intent(SelectSubjectActivity.this, QuizActivity.class);
                intent.putExtra(Constants.SUBJECT, subject);
                startActivity(intent);
                finish();
            });

            rvSelectedSubject.setAdapter(adapter);
        }
    }
}
