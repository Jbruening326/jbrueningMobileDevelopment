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
    public static int numAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Repository repository = new Repository(getApplication());

        if(repository.getAllInstructors().size() == 0){
            Instructor instructor1 = new Instructor(0, "Phil",
                    "330-123-8765",
                    "phil@school.com");
            Instructor instructor2 = new Instructor(0, "Jerry",
                    "330-321-5678",
                    "jerry@school.com");
            Instructor instructor3 = new Instructor(0, "Hannah",
                    "330-213-7658",
                    "hannah@school.com");
            Instructor instructor4 = new Instructor(0, "Tim",
                    "330-213-5786",
                    "tim@school.com");
            repository.insert(instructor1);
            repository.insert(instructor2);
            repository.insert(instructor3);
            repository.insert(instructor4);
        }


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
                        term1.getStartDate(), DateHelper.makeDateString(28,2,2023),
                        "Hard course", completed, 1, 1);
                Course course2 = new Course(0, "Advanced Data Management",
                        DateHelper.makeDateString(28, 28, 2023), term1.getEndDate(),
                        "Easy Course", inProgress, 1, 2);

                repository.insert(course1);
                repository.insert(course2);


                //Sample assessment data
                String oA = "Objective";
                String pA = "Performance";

                Assessment assessment1 = new Assessment(0, oA, oA,
                        term1.getStartDate(),
                        DateHelper.makeDateString(15, 1, 2023),1);
                Assessment assessment2 = new Assessment(0, "oA2", oA,
                        term1.getStartDate(),
                        DateHelper.makeDateString(1, 2, 2023),1);
                Assessment assessment3 = new Assessment(0, pA, pA,
                        term1.getStartDate(),
                        DateHelper.makeDateString(15, 2, 2023),1);
                Assessment assessment4 = new Assessment(0, oA, oA,
                        DateHelper.makeDateString(28, 28, 2023),
                        DateHelper.makeDateString(15, 3, 2023),2);
                Assessment assessment5 = new Assessment(0, pA, pA,
                        DateHelper.makeDateString(28, 28, 2023),
                        DateHelper.makeDateString(31, 5, 2023),2);


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