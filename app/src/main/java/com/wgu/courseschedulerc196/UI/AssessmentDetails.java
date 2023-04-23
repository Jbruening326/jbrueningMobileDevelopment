package com.wgu.courseschedulerc196.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.Helper.MyReceiver;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;

import java.util.Calendar;
import java.util.Date;

public class AssessmentDetails extends AppCompatActivity {

    private EditText editTextAssessmentName;
    private RadioButton performanceButton;
    private RadioButton objectiveButton;
    private Button startDateButton;
    private DatePickerDialog startDate;
    private Button endDateButton;
    private DatePickerDialog endDate;

    private final Repository repository = new Repository(getApplication());

    private int id;
    private String name;
    private String type;
    private String sDate;
    private String eDate;
    private int courseId;


    @SuppressLint("MissingInflatedId")
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

        startDateButton = findViewById(R.id.aStartDateButton);
        startDate = DateHelper.initDatePicker(startDateButton);
        sDate = getIntent().getStringExtra("start");


        endDateButton = findViewById(R.id.assessmentDateButton);
        endDate = DateHelper.initDatePicker(endDateButton);
        eDate = getIntent().getStringExtra("end");

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
        startDateButton.setText(sDate);
        endDateButton.setText(eDate);
    }

    public void onStartDateSelect(View view) {
        startDate.show();
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

        int startYear = startDate.getDatePicker().getYear();
        int startMonth = startDate.getDatePicker().getMonth();
        int startDay = startDate.getDatePicker().getDayOfMonth();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);

        int endYear = endDate.getDatePicker().getYear();
        int endMonth = endDate.getDatePicker().getMonth();
        int endDay = endDate.getDatePicker().getDayOfMonth();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);


        sDate = startDateButton.getText().toString();
        eDate = endDateButton.getText().toString();
        if (courseId == -1) {
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

        if (endCalendar.compareTo(startCalendar) < 0) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
        }
        else if(startCalendar.compareTo(cStartCalendar) < 0 || endCalendar.compareTo(cEndCalendar) > 0 || startCalendar.compareTo(cEndCalendar) > 0){
            Toast.makeText(this, "Date must fall within course dates", Toast.LENGTH_SHORT).show();
        }
        else if (name.equals("") || type.equals("") || eDate.equals("")) {
            Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show();
        } else {
            if (id == -1) {
                Assessment assessment = new Assessment(0, name, type, course.getStartDate(), eDate, courseId);
                repository.insert(assessment);
                Toast.makeText(this, "Assessment added", Toast.LENGTH_LONG).show();

            } else {
                Assessment assessment = new Assessment(id, name, type, course.getStartDate(), eDate, courseId);
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
            case R.id.assessmentStartNotify:
                if(id == -1){
                    Toast.makeText(this, "Notification not set. Select an existing assessment first", Toast.LENGTH_LONG).show();
                }
                else {
                    String startDate = startDateButton.getText().toString();
                    Date myDate = DateHelper.makeStringDate(startDate);
                    Long startTrigger = myDate.getTime();

                    Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                    intent.putExtra("notification", editTextAssessmentName.getText().toString() + ", starts today");
                    PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarManager.set(AlarmManager.RTC_WAKEUP, startTrigger, sender);
                    Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.assessmentEndNotify:
                if(id == -1){
                    Toast.makeText(this, "Notification not set. Select an existing assessment first", Toast.LENGTH_LONG).show();
                }
                else {
                    String startDate = startDateButton.getText().toString();
                    Date myDate = DateHelper.makeStringDate(startDate);
                    Long startTrigger = myDate.getTime();

                    Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                    intent.putExtra("notification", editTextAssessmentName.getText().toString() + ", ends today");
                    PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarManager.set(AlarmManager.RTC_WAKEUP, startTrigger, sender);
                    Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}