package com.wgu.courseschedulerc196.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    Course getCourse(int courseId);
}
