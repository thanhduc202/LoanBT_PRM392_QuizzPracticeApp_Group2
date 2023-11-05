package com.example.prm392_quizapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_quizapp.adapter.SubjectAdapter;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.UserDatabase;
import com.example.prm392_quizapp.data.UserDatabaseClient;

import java.util.ArrayList;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {

    private EditText ptSubjectName;
    private Button btnSave;
    private RecyclerView rvSelectedSubject;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);

        ptSubjectName = findViewById(R.id.pt_add_subject);
        btnSave = findViewById(R.id.btn_add_subject);
        rvSelectedSubject = findViewById(R.id.rv_subject_list);

        findViewById(R.id.imageViewSubject).setOnClickListener(view -> finish());

        btnSave.setOnClickListener(view -> {
            String subjectName = ptSubjectName.getText().toString();
            if(subjectName.isEmpty()){
                Toast.makeText(this, "Subject Name is required!!", Toast.LENGTH_SHORT).show();
            }else{
                Subject subject = new Subject(subjectName);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            UserDatabaseClient.getInstance(SubjectManagementActivity.this).subjectDao().insertSubject(subject);
                            runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Add new Subject successfully!!!", Toast.LENGTH_SHORT).show());
                        } catch (Exception ex) {
                            runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Add new Subject failed!!!", Toast.LENGTH_SHORT).show());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        new SubjectManagementActivity.GetAllSelectedSubjectTask().execute();
                    }
                }.execute();
            }
        });

        new SubjectManagementActivity.GetAllSelectedSubjectTask().execute();

    }

    @SuppressLint("StaticFieldLeak")
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
            SubjectAdapter adapter = new SubjectAdapter(subjects, UserDatabaseClient.getInstance(getApplicationContext()).subjectDao());

            adapter.setOnUpdateClickListener(new SubjectAdapter.OnUpdateClickListener() {
                @Override
                public void onUpdateClick(Subject subject) {
                    // Tạo một Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubjectManagementActivity.this);
                    builder.setTitle("Update Subject");

                    // Tạo một EditText để người dùng nhập tên mới cho Subject
                    final EditText input = new EditText(SubjectManagementActivity.this);
                    input.setText(subject.getSubjectName());
                    builder.setView(input);

                    // Thiết lập nút Update
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newSubjectName = input.getText().toString();
                            if (!newSubjectName.isEmpty()) {
                                // Cập nhật Subject trong cơ sở dữ liệu
                                subject.setSubjectName(newSubjectName);
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {
                                            UserDatabaseClient.getInstance(SubjectManagementActivity.this).subjectDao().updateSubject(subject);
                                            runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Update Subject successfully!!!", Toast.LENGTH_SHORT).show());
                                        } catch (Exception ex) {
                                            runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Update Subject failed!!!", Toast.LENGTH_SHORT).show());
                                        }

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        // Cập nhật RecyclerView sau khi cập nhật Subject
                                        new GetAllSelectedSubjectTask().execute();
                                    }
                                }.execute();
                            }
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
            adapter.setOnDeleteClickListener(new SubjectAdapter.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(Subject subject) {
                    new AlertDialog.Builder(SubjectManagementActivity.this)
                            .setTitle("Delete Subject")
                            .setMessage("Are you sure you want to delete this subject?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            try {
                                                UserDatabaseClient.getInstance(SubjectManagementActivity.this).subjectDao().deleteSubject(subject);
                                                runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Delete Subject successfully!!!", Toast.LENGTH_SHORT).show());
                                            } catch (Exception ex) {
                                                runOnUiThread(() -> Toast.makeText(SubjectManagementActivity.this, "Delete Subject failed!!!", Toast.LENGTH_SHORT).show());
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            new GetAllSelectedSubjectTask().execute();
                                        }
                                    }.execute();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            });

            rvSelectedSubject.setAdapter(adapter);
        }
    }
}
