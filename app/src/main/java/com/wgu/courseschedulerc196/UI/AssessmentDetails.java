package com.wgu.courseschedulerc196.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.Calendar;
import java.util.Date;

public class AssessmentDetails extends AppCompatActivity {

    private EditText editTextAssessmentName;
    private RadioButton performanceButton;
    private RadioButton objectiveButton;
    private Button endDateButton;
    private DatePickerDialog endDate;

    private final Repository repository = new Repository(getApplication());

    private int id;
    private String name;
    private String type;
    private String date;
    private int courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        id = getIntent().getIntExtra("id", -1);

        editTextAssessmentName = findViewById(R.id.editTextAssessmentName);
        name = getIntent().getStringExtra("title");

        performanceButton = findViewById(R.id.performanceRadioButton);
        objectiveButton = findViewById(R.id.objectiveRadioButton);
        type = getIntent().getStringExtra("type");

        endDateButton = findViewById(R.id.assessmentDateButton);
        endDate = DateHelper.initDatePicker(endDateButton);
        date = getIntent().getStringExtra("end");

        courseId = getIntent().getIntExtra("courseId", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextAssessmentName.setText(name);
        if (type != null) {
            if (type.equals("Performance")) {
                performanceButton.toggle();
            } else {
                objectiveButton.toggle();
            }
        } else {
            performanceButton.toggle();
        }
        endDateButton.setText(date);
    }

    public void onEndDateSelect(View view) {
        endDate.show();
    }


    public void onSaveClick(View view) {
        id = getIntent().getIntExtra("id", -1);
        name = editTextAssessmentName.getText().toString();
        if (performanceButton.isChecked()) {
            type = performanceButton.getText().toString();
        } else if (objectiveButton.isChecked()) {
            type = objectiveButton.getText().toString();
        } else {
            type = "";
        }

        int endYear = endDate.getDatePicker().getYear();
        int endMonth = endDate.getDatePicker().getMonth();
        int endDay = endDate.getDatePicker().getDayOfMonth();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);




        date = endDateButton.getText().toString();
        if (courseId == -1){
            courseId = getIntent().getIntExtra("courseIdOnly", -1);
        }

        Course course = repository.getCourse(courseId);
        String courseStart = course.getStartDate();
        String courseEnd = course.getEndDate();
        Date cStartDate = DateHelper.makeStringDate(courseStart);
        Calendar cStartCalendar = Calendar.getInstance();
        cStartCalendar.setTime(cStartDate);
        Date cEndDate = DateHelper.makeStringDate(courseEnd);
        Calendar cEndCalendar = Calendar.getInstance();
        cEndCalendar.setTime(cEndDate);

        if(endCalendar.compareTo(cStartCalendar) < 0 || endCalendar.compareTo(cEndCalendar) > 0){
            Toast.makeText(this, "Assessment date must be within course date", Toast.LENGTH_SHORT).show();
        }

        else if (name.equals("") || type.equals("") || date.equals("")) {
            Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show();
        }

        else {
            if (id == -1) {
                Assessment assessment = new Assessment(0, name, type, date, courseId);
                repository.insert(assessment);
                Toast.makeText(this, "Assessment added", Toast.LENGTH_LONG).show();

            } else {
                Assessment assessment = new Assessment(id, name, type, date, courseId);
                repository.update(assessment);
                Toast.makeText(this, "Assessment updated", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAssessment:
                id = getIntent().getIntExtra("id", -1);
                if (id != -1) {
                    Assessment assessmentToDelete = repository.getAssessment(id);
                    repository.delete(assessmentToDelete);
                    Toast.makeText(this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No Assessment Selected", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}