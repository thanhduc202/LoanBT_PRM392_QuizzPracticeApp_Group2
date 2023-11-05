package com.example.prm392_quizapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.prm392_quizapp.dao.QuizDao;
import com.example.prm392_quizapp.dao.SubjectDao;
import com.example.prm392_quizapp.dao.UserDao;


@Database(
        entities = {User.class,Quiz.class, Subject.class,Attempt.class},
        exportSchema = false,
        version = 1
)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract QuizDao quizDao();

    public  abstract SubjectDao subjectDao();
}