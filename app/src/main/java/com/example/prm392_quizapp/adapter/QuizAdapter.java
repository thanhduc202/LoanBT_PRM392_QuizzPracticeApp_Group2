package com.example.prm392_quizapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_quizapp.R;
import com.example.prm392_quizapp.dao.QuizDao;
import com.example.prm392_quizapp.data.Quiz;
import com.example.prm392_quizapp.data.QuizWithSubject;
import com.example.prm392_quizapp.data.Subject;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.SelectedViewHolder>{
    private final List<Quiz> quizList;

    private final QuizDao quizDao;

    public QuizAdapter(List<Quiz> quizList, QuizDao quizDao) {
        this.quizList = quizList;
        this.quizDao = quizDao;
    }

    public interface OnUpdateQuizClickListener {
        void onUpdateClick(Quiz quiz);
    }
    private OnUpdateQuizClickListener onUpdateQuizClickListener;

    // Thêm một phương thức để thiết lập listener
    public void setOnUpdateQuizClickListener(OnUpdateQuizClickListener listener) {
        this.onUpdateQuizClickListener = listener;
    }

    public interface OnDeleteQuizClickListener {
        void onDeleteClick(Quiz quiz);
    }
    // Trong Adapter, khai báo một instance của interface
    private OnDeleteQuizClickListener onDeleteQuizClickListener;

    // Thêm một phương thức để thiết lập listener
    public void setOnDeleteQuizClickListener(OnDeleteQuizClickListener listener) {
        this.onDeleteQuizClickListener = listener;
    }

    @NonNull
    @Override
    public SelectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_quiz, parent,false);
        return new QuizAdapter.SelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);

        holder.txtQuizQuestion.setText(quiz.getQuestion());
        //Thêm sự kiện nút update và delete

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUpdateQuizClickListener != null) {
                    onUpdateQuizClickListener.onUpdateClick(quiz);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteQuizClickListener != null) {
                    onDeleteQuizClickListener.onDeleteClick(quiz);
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
        return quizList.size();
    }

    public class SelectedViewHolder extends RecyclerView.ViewHolder{

        public CardView cvParent;
        public TextView txtQuizQuestion;

        public Button btnUpdate, btnDelete;

        public SelectedViewHolder(View v){
            super(v);
            cvParent = v.findViewById(R.id.cvQuizzes);
            txtQuizQuestion = v.findViewById(R.id.tv_quiz_question);
            btnUpdate = v.findViewById(R.id.btn_update_quiz);
            btnDelete = v.findViewById(R.id.btn_delete_quiz);
        }
    }
}
