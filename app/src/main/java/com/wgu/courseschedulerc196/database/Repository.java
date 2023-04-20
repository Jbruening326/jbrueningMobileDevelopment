package com.wgu.courseschedulerc196.database;

import android.app.Application;

import com.wgu.courseschedulerc196.dao.AssessmentDAO;
import com.wgu.courseschedulerc196.dao.CourseDAO;
import com.wgu.courseschedulerc196.dao.InstructorDAO;
import com.wgu.courseschedulerc196.dao.TermDAO;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Instructor;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private AssessmentDAO mAssessmentDAO;
    private List<Assessment> mAllAssessments;

    private CourseDAO mCourseDAO;
    private List<Course> mAllCourses;

    private InstructorDAO mInstructorDAO;
    private List<Instructor> mAllInstructors;
    private Instructor mInstructor;

    private TermDAO mTermDAO;
    private List<Term> mAllTerms;
    private Term mTerm;
    private Course mCourse;
    private Assessment mAssessment;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        CourseDatabaseBuilder db = CourseDatabaseBuilder.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();
        mCourseDAO = db.courseDAO();
        mInstructorDAO = db.instructorDAO();
        mTermDAO = db.termDAO();
    }

    //Assessment
    public List<Assessment>getAllAssessments(){
        databaseExecutor.execute(() -> {
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    public void insert(Assessment assessment){
        databaseExecutor.execute(() -> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment){
        databaseExecutor.execute(() -> {
            mAssessmentDAO.update(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment){
        databaseExecutor.execute(() -> {
            mAssessmentDAO.delete(assessment);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Assessment getAssessment(int assessmentId) {
        databaseExecutor.execute(() -> {
            mAssessment = mAssessmentDAO.getAssessment(assessmentId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAssessment;
    }

    //Courses
    public List<Course>getAllCourses(){
        databaseExecutor.execute(() -> {
            mAllCourses = mCourseDAO.getAllCourses();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public void insert(Course course){
        databaseExecutor.execute(() -> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course){
        databaseExecutor.execute(() -> {
            mCourseDAO.update(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course){
        databaseExecutor.execute(() -> {
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Course getCourse(int courseId) {
        databaseExecutor.execute(() -> {
            mCourse = mCourseDAO.getCourse(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mCourse;
    }


    //Instructors
    public List<Instructor>getAllInstructors(){
        databaseExecutor.execute(() -> {
            mAllInstructors = mInstructorDAO.getAllInstructors();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllInstructors;
    }

    public void insert(Instructor instructor){
        databaseExecutor.execute(() -> {
            mInstructorDAO.insert(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Instructor instructor){
        databaseExecutor.execute(() -> {
            mInstructorDAO.update(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Instructor instructor){
        databaseExecutor.execute(() -> {
            mInstructorDAO.delete(instructor);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Instructor getInstructor(int instructorId) {
        databaseExecutor.execute(() -> {
            mInstructor = mInstructorDAO.getInstructor(instructorId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mInstructor;
    }

    //Terms
    public List<Term>getAllTerms(){
        databaseExecutor.execute(() -> {
            mAllTerms = mTermDAO.getAllTerms();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    public void insert(Term term){
        databaseExecutor.execute(() -> {
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term){
        databaseExecutor.execute(() -> {
            mTermDAO.update(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term){
        databaseExecutor.execute(() -> {
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Term getTerm(int termId) {
        databaseExecutor.execute(() -> {
            mTerm = mTermDAO.getTerm(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mTerm;
    }

}
