package com.wgu.courseschedulerc196.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wgu.courseschedulerc196.dao.AssessmentDAO;
import com.wgu.courseschedulerc196.dao.CourseDAO;
import com.wgu.courseschedulerc196.dao.InstructorDAO;
import com.wgu.courseschedulerc196.dao.TermDAO;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Instructor;
import com.wgu.courseschedulerc196.entities.Term;

@Database(entities = {Assessment.class, Course.class, Instructor.class, Term.class}, version = 15, exportSchema = false)
public abstract class CourseDatabaseBuilder extends RoomDatabase {
    public abstract AssessmentDAO assessmentDAO();
    public abstract CourseDAO courseDAO();
    public abstract InstructorDAO instructorDAO();
    public abstract TermDAO termDAO();

    private static volatile CourseDatabaseBuilder INSTANCE;

    static CourseDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (CourseDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(), CourseDatabaseBuilder.class, "MyCourseDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
