package com.example.prm392_quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prm392_quizapp.adapter.QuizAdapter;
import com.example.prm392_quizapp.adapter.SubjectListAdapter;
import com.example.prm392_quizapp.data.Quiz;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.UserDatabase;
import com.example.prm392_quizapp.data.UserDatabaseClient;

import java.util.ArrayList;
import java.util.List;

public class QuizManagementActivity extends AppCompatActivity {

    private Button btnAdd;
    private RecyclerView rvQuizzes;
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_management);

        btnAdd = findViewById(R.id.btn_add_quiz);
        rvQuizzes = findViewById(R.id.rv_quiz_list);

        findViewById(R.id.imageViewQuiz).setOnClickListener(view -> finish());


        btnAdd.setOnClickListener(view -> {
            //Add quiz
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizManagementActivity.this);
            builder.setTitle("Add Quiz");

            // Sử dụng layout mới cho Dialog
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_quiz, null);
            builder.setView(dialogView);

            // Tìm các view trong layout
            final EditText questionInput = dialogView.findViewById(R.id.question_input);
            final EditText answer1Input = dialogView.findViewById(R.id.answer1_input);
            final EditText answer2Input = dialogView.findViewById(R.id.answer2_input);
            final EditText answer3Input = dialogView.findViewById(R.id.answer3_input);
            final EditText answer4Input = dialogView.findViewById(R.id.answer4_input);
            final RadioGroup correctInput = dialogView.findViewById(R.id.radio_group);
            final Spinner subjectInput = dialogView.findViewById(R.id.subject_input);

            new AsyncTask<Void, Void, List<Subject>>() {
                @Override
                protected List<Subject> doInBackground(Void... voids) {
                    return UserDatabaseClient.getInstance(QuizManagementActivity.this).subjectDao().getAllSubjects();
                }

                @Override
                protected void onPostExecute(List<Subject> subjects) {
                    SubjectListAdapter subjectAdapter = new SubjectListAdapter(QuizManagementActivity.this, subjects);
                    subjectInput.setAdapter(subjectAdapter);
                }
            }.execute();
            // Thiết lập nút Add
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Kiểm tra xem các EditText đã được điền chưa
                    if (questionInput.getText().toString().trim().isEmpty() ||
                            answer1Input.getText().toString().trim().isEmpty() ||
                            answer2Input.getText().toString().trim().isEmpty() ||
                            answer3Input.getText().toString().trim().isEmpty() ||
                            answer4Input.getText().toString().trim().isEmpty()) {
                        Toast.makeText(QuizManagementActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra xem RadioGroup đã chọn chưa
                    if (correctInput.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(QuizManagementActivity.this, "Please select the correct answer", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Thêm Quiz vào Database

                        // Code để thêm Quiz vào Database
                        String correctAnswer = "";
                        switch (correctInput.getCheckedRadioButtonId()){
                            case R.id.radio_answer1:
                                correctAnswer = answer1Input.getText().toString();
                                break;
                            case R.id.radio_answer2:
                                correctAnswer = answer2Input.getText().toString();
                                break;
                            case R.id.radio_answer3:
                                correctAnswer = answer3Input.getText().toString();
                                break;
                            case R.id.radio_answer4:
                                correctAnswer = answer4Input.getText().toString();
                                break;

                        }
                        Quiz quiz = new Quiz(
                                questionInput.getText().toString(),
                                answer1Input.getText().toString(),
                                answer2Input.getText().toString(),
                                answer3Input.getText().toString(),
                                answer4Input.getText().toString(),
                                correctAnswer,
                                (int) subjectInput.getSelectedItemId()
                        );
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                UserDatabaseClient.getInstance(QuizManagementActivity.this).quizDao().insertQuiz(quiz);
                                runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "Quiz added successfully!!!", Toast.LENGTH_SHORT).show());
                            } catch (Exception ex) {
                                runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "An error occurred while adding the Quiz", Toast.LENGTH_SHORT).show());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            new GetAllSelectedQuizTask().execute();
                        }
                    }.execute();
                }
            });

            // Thiết lập nút Cancel
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Hiển thị Dialog
            builder.show();
        });

        new GetAllSelectedQuizTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class GetAllSelectedQuizTask extends AsyncTask<Void, Void, Void>{
        List<Quiz> quizzes = new ArrayList<>();

        public GetAllSelectedQuizTask() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            quizzes = databaseClient.quizDao().getAllQuiz();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            QuizAdapter adapter = new QuizAdapter(quizzes, UserDatabaseClient.getInstance(getApplicationContext()).quizDao());
            adapter.setOnUpdateQuizClickListener(new QuizAdapter.OnUpdateQuizClickListener() {
                @Override
                public void onUpdateClick(Quiz quiz) {
                    //Update Quiz
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizManagementActivity.this);
                    builder.setTitle("Add Quiz");

                    // Sử dụng layout mới cho Dialog
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_quiz, null);
                    builder.setView(dialogView);

                    // Tìm các view trong layout
                    final EditText questionInput = dialogView.findViewById(R.id.question_input);
                    questionInput.setText(quiz.getQuestion());

                    final EditText answer1Input = dialogView.findViewById(R.id.answer1_input);
                    answer1Input.setText(quiz.getAnswer1());

                    final EditText answer2Input = dialogView.findViewById(R.id.answer2_input);
                    answer2Input.setText(quiz.getAnswer2());

                    final EditText answer3Input = dialogView.findViewById(R.id.answer3_input);
                    answer3Input.setText(quiz.getAnswer3());

                    final EditText answer4Input = dialogView.findViewById(R.id.answer4_input);
                    answer4Input.setText(quiz.getAnswer4());

                    final RadioGroup correctInput = dialogView.findViewById(R.id.radio_group);
                    if(quiz.getCorrectAnswer().equals(quiz.getAnswer1())){
                        RadioButton radioButton = dialogView.findViewById(R.id.radio_answer1);
                        radioButton.setChecked(true);
                    }else
                    if (quiz.getCorrectAnswer().equals(quiz.getAnswer2())){
                        RadioButton radioButton = dialogView.findViewById(R.id.radio_answer2);
                        radioButton.setChecked(true);
                    }else
                    if (quiz.getCorrectAnswer().equals(quiz.getAnswer3())){
                        RadioButton radioButton = dialogView.findViewById(R.id.radio_answer3);
                        radioButton.setChecked(true);
                    } else {
                        RadioButton radioButton = dialogView.findViewById(R.id.radio_answer4);
                        radioButton.setChecked(true);
                    }

                    final Spinner subjectInput = dialogView.findViewById(R.id.subject_input);
                    //Set selected Subject

                    new AsyncTask<Void, Void, List<Subject>>() {
                        @Override
                        protected List<Subject> doInBackground(Void... voids) {
                            return UserDatabaseClient.getInstance(QuizManagementActivity.this).subjectDao().getAllSubjects();
                        }

                        @Override
                        protected void onPostExecute(List<Subject> subjects) {
                            SubjectListAdapter subjectAdapter = new SubjectListAdapter(QuizManagementActivity.this, subjects);
                            subjectInput.setAdapter(subjectAdapter);
                            // Tìm vị trí của Subject trong danh sách
                            for (int i = 0; i < subjects.size(); i++) {
                                if (subjects.get(i).getSubjectID() == quiz.getSubjectID()) {
                                    subjectInput.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }.execute();
                    // Thiết lập nút Add
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Kiểm tra xem các EditText đã được điền chưa
                            if (questionInput.getText().toString().trim().isEmpty() ||
                                    answer1Input.getText().toString().trim().isEmpty() ||
                                    answer2Input.getText().toString().trim().isEmpty() ||
                                    answer3Input.getText().toString().trim().isEmpty() ||
                                    answer4Input.getText().toString().trim().isEmpty()) {
                                Toast.makeText(QuizManagementActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Kiểm tra xem RadioGroup đã chọn chưa
                            if (correctInput.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(QuizManagementActivity.this, "Please select the correct answer", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Update Quiz vào Database

                            String correctAnswer = "";
                            switch (correctInput.getCheckedRadioButtonId()){
                                case R.id.radio_answer1:
                                    correctAnswer = answer1Input.getText().toString();
                                    break;
                                case R.id.radio_answer2:
                                    correctAnswer = answer2Input.getText().toString();
                                    break;
                                case R.id.radio_answer3:
                                    correctAnswer = answer3Input.getText().toString();
                                    break;
                                case R.id.radio_answer4:
                                    correctAnswer = answer4Input.getText().toString();
                                    break;

                            }
                            quiz.setQuestion(questionInput.getText().toString());
                            quiz.setAnswer1(answer1Input.getText().toString());
                            quiz.setAnswer2(answer2Input.getText().toString());
                            quiz.setAnswer3(answer3Input.getText().toString());
                            quiz.setAnswer4(answer4Input.getText().toString());
                            quiz.setCorrectAnswer(correctAnswer);
                            quiz.setSubjectID((int) subjectInput.getSelectedItemId());

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        UserDatabaseClient.getInstance(QuizManagementActivity.this).quizDao().updateQuiz(quiz);
                                        runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "Quiz updated successfully!!!", Toast.LENGTH_SHORT).show());
                                    } catch (Exception ex) {
                                        runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "An error occurred while updating the Quiz", Toast.LENGTH_SHORT).show());
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    new GetAllSelectedQuizTask().execute();
                                }
                            }.execute();
                        }
                    });

                    // Thiết lập nút Cancel
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Hiển thị Dialog
                    builder.show();
                }
            });

            adapter.setOnDeleteQuizClickListener(new QuizAdapter.OnDeleteQuizClickListener() {
                @Override
                public void onDeleteClick(Quiz quiz) {
                    //Delete Quiz
                    new AlertDialog.Builder(QuizManagementActivity.this)
                            .setTitle("Delete Quiz")
                            .setMessage("Are you sure you want to delete this quiz?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            try {
                                                UserDatabaseClient.getInstance(QuizManagementActivity.this).quizDao().deleteQuiz(quiz);
                                                runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "Delete Quiz successfully!!!", Toast.LENGTH_SHORT).show());
                                            } catch (Exception ex) {
                                                runOnUiThread(() -> Toast.makeText(QuizManagementActivity.this, "Delete Quiz failed!!!", Toast.LENGTH_SHORT).show());
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            new GetAllSelectedQuizTask().execute();
                                        }
                                    }.execute();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            rvQuizzes.setAdapter(adapter);
        }
    }
}