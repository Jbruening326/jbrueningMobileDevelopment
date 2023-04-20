package com.wgu.courseschedulerc196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Instructor;
import com.wgu.courseschedulerc196.entities.Term;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEnterClick(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_sample_data, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addSampleData:
                Repository repository = new Repository(getApplication());


                //Sample term data
                String startDate = DateHelper.makeDateString(1, 1, 2023);
                String endDate = DateHelper.makeDateString(30, 6, 2023);
                Term term1 = new Term(0, "Term 1", startDate, endDate);
                //Term term2 = new Term(0, "Term 2", "2023-06-21", "2023-12-31");

                repository.insert(term1);
                //repository.insert(term2);

                //Sample course data
                String inProgress = "In Progress";
                String completed = "Completed";
                String dropped = "Dropped";
                String planToTake = "Plan To Take";

                Course course1 = new Course(0, "Data Structures and Algorithms",
                        term1.getStartDate(), "2023-02-28",
                        "Hard course", completed, 1, 1);
                Course course2 = new Course(0, "Advanced Data Management",
                        "2023-02-28", term1.getEndDate(),
                        "Easy Course", inProgress, 1, 2);
                /*Course course3 = new Course(0, "Software 1",
                        term2.getStartDate(), "2023-08-31",
                        "I can do this", planToTake, term2.getTermId());*/
                /*Course course4 = new Course(0, "Capstone",
                        course3.getEndDate(), term2.getEndDate(),
                        "Last course", planToTake, term2.getTermId());*/
                repository.insert(course1);
                repository.insert(course2);
                //repository.insert(course3);
                //repository.insert(course4);

                //Sample instructor data
                Instructor instructor1 = new Instructor(0, "Phil",
                        "3303303330",
                        "phil@school.com");
                Instructor instructor2 = new Instructor(0, "Jerry",
                        "3303303330",
                        "jerry@school.com");
                Instructor instructor3 = new Instructor(0, "Hannah",
                        "3303303330",
                        "hannah@school.com");
                Instructor instructor4 = new Instructor(0, "Tim",
                        "3303303330",
                        "tim@school.com");
                repository.insert(instructor1);
                repository.insert(instructor2);
                repository.insert(instructor3);
                repository.insert(instructor4);

                //Sample assessment data
                String oA = "Objective";
                String pA = "Performance";

                Assessment assessment1 = new Assessment(0, oA, oA, "2023-01-15", 1);
                Assessment assessment2 = new Assessment(0, "oA2", oA, "2023-02-01", 1);
                Assessment assessment3 = new Assessment(0, pA, pA, "2023-02-15", 1);

                Assessment assessment4 = new Assessment(0, oA, oA, "2023-03-15", 2);
                Assessment assessment5 = new Assessment(0, pA, pA, "2023-05-31", 2);

                //Assessment assessment6 = new Assessment(0, oA, oA, "2023-07-15", course2.getCourseId());
                //Assessment assessment7 = new Assessment(0, pA, pA, "2023-08-15", course2.getCourseId());

                //Assessment assessment8 = new Assessment(0, oA, oA, "2023-09-15", course2.getCourseId());
                //Assessment assessment9 = new Assessment(0, pA, pA, "2023-11-31", course2.getCourseId());

                repository.insert(assessment1);
                repository.insert(assessment2);
                repository.insert(assessment3);
                repository.insert(assessment4);
                repository.insert(assessment5);
                //repository.insert(assessment6);
                //repository.insert(assessment7);
                //repository.insert(assessment8);
                //repository.insert(assessment9);

        }
        return super.onOptionsItemSelected(item);
    }
}