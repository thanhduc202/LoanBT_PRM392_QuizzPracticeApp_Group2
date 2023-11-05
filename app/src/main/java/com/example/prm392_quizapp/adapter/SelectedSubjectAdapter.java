package com.example.prm392_quizapp.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_quizapp.R;
import com.example.prm392_quizapp.dao.SubjectDao;
import com.example.prm392_quizapp.data.Subject;

import java.util.List;

public class SelectedSubjectAdapter extends RecyclerView.Adapter<SelectedSubjectAdapter.SelectedViewHolder> {

    private final List<Subject> subjects;
    private final SubjectDao subjectDao;

    public SelectedSubjectAdapter(List<Subject> subjects, SubjectDao subjectDao) {
        this.subjects = subjects;
        this.subjectDao = subjectDao;
    }

    public interface OnClickListener {
        void onClick(Subject subject);
    }
    // Trong Adapter, khai báo một instance của interface
    private OnClickListener onClickListener;

    // Thêm một phương thức để thiết lập listener
    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.selected_subject_quiz, parent,false);
        return new SelectedSubjectAdapter.SelectedViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull SelectedViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.txtSubject.setText(subject.getSubjectName());
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                // Thực hiện các thao tác với cơ sở dữ liệu ở đây
                return subjectDao.countQuizBySubject(subject.getSubjectID());
            }

            @Override
            protected void onPostExecute(Integer count) {
                holder.txtCountQuiz.setText(String.valueOf(count));
            }
        }.execute();


        /*holder.txtCountQuiz.setText(subjectDao.countQuizBySubject(subject.getSubjectID()));*/
        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO NOTHING
                if (onClickListener != null) {
                    onClickListener.onClick(subject);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class SelectedViewHolder extends RecyclerView.ViewHolder{

        public CardView cvParent;
        public TextView txtSubject, txtCountQuiz;

        public SelectedViewHolder(View v){
            super(v);
            cvParent = v.findViewById(R.id.cvItemSubjectQuiz);
            txtSubject = v.findViewById(R.id.tv_selected_subject);
            txtCountQuiz = v.findViewById(R.id.tv_total_quiz);
        }
    }
}

