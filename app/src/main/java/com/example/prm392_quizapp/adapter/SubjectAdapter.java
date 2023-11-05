package com.example.prm392_quizapp.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_quizapp.R;
import com.example.prm392_quizapp.SubjectManagementActivity;
import com.example.prm392_quizapp.dao.SubjectDao;
import com.example.prm392_quizapp.data.Subject;
import com.example.prm392_quizapp.data.UserDatabaseClient;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SelectedViewHolder>{
    private final List<Subject> subjects;

    private final SubjectDao subjectDao;

    public interface OnUpdateClickListener {
        void onUpdateClick(Subject subject);
    }
    private OnUpdateClickListener onUpdateClickListener;

    // Thêm một phương thức để thiết lập listener
    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Subject subject);
    }
    // Trong Adapter, khai báo một instance của interface
    private OnDeleteClickListener onDeleteClickListener;

    // Thêm một phương thức để thiết lập listener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public SubjectAdapter(List<Subject> subjects, SubjectDao subjectDao) {
        this.subjects = subjects;
        this.subjectDao = subjectDao;
    }
    @NonNull
    @Override
    public SubjectAdapter.SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_subject, parent,false);
        return new SubjectAdapter.SelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SelectedViewHolder holder, int position) {
        Subject subject = subjects.get(position);

        holder.txtSubject.setText(subject.getSubjectName());
        //Thêm sự kiện nút update và delete

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(subject);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(subject);
                }
            }
        });


        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class SelectedViewHolder extends RecyclerView.ViewHolder{

        public CardView cvParent;
        public TextView txtSubject;

        public Button btnUpdate, btnDelete;

        public SelectedViewHolder(View v){
            super(v);
            cvParent = v.findViewById(R.id.cvSubjects);
            txtSubject = v.findViewById(R.id.tv_subject_name);
            btnUpdate = v.findViewById(R.id.btn_update_subject);
            btnDelete = v.findViewById(R.id.btn_delete_subject);
        }
    }
}

