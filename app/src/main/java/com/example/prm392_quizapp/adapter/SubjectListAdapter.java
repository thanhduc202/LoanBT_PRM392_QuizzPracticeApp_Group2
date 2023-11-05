package com.example.prm392_quizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm392_quizapp.R;
import com.example.prm392_quizapp.data.Subject;

import java.util.List;

public class SubjectListAdapter extends ArrayAdapter<Subject> {

    List<Subject> subjects;

    public SubjectListAdapter(Context context, List<Subject> subjects) {
        super(context, R.layout.item_subject_list, subjects);
        this.subjects = subjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_subject_list, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.tv_subject_name_list);
        Subject subject = subjects.get(position);
        if (subject != null) {
            textView.setText(subject.getSubjectName());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        Subject subject = subjects.get(position);
        return subject != null ? subject.getSubjectID() : -1;
    }
}
