package com.wgu.courseschedulerc196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.Helper.MyReceiver;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Instructor;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetails extends AppCompatActivity {

    private EditText editName;
    private DatePickerDialog editStart;
    private DatePickerDialog editEnd;
    private Button startDateButton;
    private Button endDateButton;


    private Spinner statusSpinner;
    private Spinner instructorSpinner;
    private TextView instructorPhoneView;
    private TextView instructorEmailView;
    private EditText notes;

    private Repository repository = new Repository(getApplication());

    private int id;
    private String title;
    private String startDate;
    private String endDate;
    private final List<String> statusTypes = new ArrayList<>();
    private String status;
    private final List<Instructor> listInstructors = new ArrayList<>();
    private final List<String> stringInstructors = new ArrayList<>();
    private String stringInstructor;
    private Instructor instructor;
    private String note;
    private int termId;
    private int instructorId;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        id = getIntent().getIntExtra("id", -1);

        editName = findViewById(R.id.editTextCourseName);
        title = getIntent().getStringExtra("title");


        startDateButton = findViewById(R.id.courseStartDateButton);
        editStart = DateHelper.initDatePicker(startDateButton);
        startDate = getIntent().getStringExtra("start");
        endDateButton = findViewById(R.id.courseEndDateButton);
        editEnd = DateHelper.initDatePicker(endDateButton);
        endDate = getIntent().getStringExtra("end");

        String completed = "Completed";
        String inProgress = "In Progress";
        String dropped = "Dropped";
        String planned = "Plan To Take";
        statusTypes.add(completed);
        statusTypes.add(inProgress);
        statusTypes.add(dropped);
        statusTypes.add(planned);
        statusSpinner = findViewById(R.id.statusSpinner);
        status = getIntent().getStringExtra("status");


        listInstructors.addAll(repository.getAllInstructors());
        for (int i = 0; i < listInstructors.size(); i++) {
            String temp = listInstructors.get(i).toString();
            stringInstructors.add(temp);
        }
        instructorSpinner = findViewById(R.id.instructorSpinner);
        instructorId = getIntent().getIntExtra("instructorId", -1);
        try {
            instructor = repository.getInstructor(instructorId);
            stringInstructor = instructor.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        instructorPhoneView = findViewById(R.id.instructorPhoneView);
        instructorEmailView = findViewById(R.id.instructorEmailView);


        notes = findViewById(R.id.notesTextView);
        note = getIntent().getStringExtra("note");

        termId = getIntent().getIntExtra("termId", -1);

        recyclerView = findViewById(R.id.assessmentRecyclerView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        editName.setText(title);
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);


        ArrayAdapter<String> statusArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusTypes);
        statusSpinner.setAdapter(statusArrayAdapter);
        int statusPosition = statusArrayAdapter.getPosition(status);
        statusSpinner.setSelection(statusPosition);


        ArrayAdapter<String> instructorArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stringInstructors);
        instructorSpinner.setAdapter(instructorArrayAdapter);
        int instructorPosition = instructorArrayAdapter.getPosition(stringInstructor);
        instructorSpinner.setSelection(instructorPosition);


        instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    stringInstructor = instructorArrayAdapter.getItem(i);
                    for (Instructor j : repository.getAllInstructors()) {
                        if (stringInstructor.equals(j.toString())) {
                            instructorId = j.getInstructorId();
                        }
                    }
                    instructor = repository.getInstructor(instructorId);
                    instructorPhoneView.setText(instructor.getPhoneNumber());
                    instructorEmailView.setText(instructor.getEmail());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        notes.setText(note);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> filteredAssessments = new ArrayList<>();
        for (Assessment a : repository.getAllAssessments()) {
            if (a.getCourseId() == id) {
                filteredAssessments.add(a);
            }
        }
        assessmentAdapter.setAssessments(filteredAssessments);
    }

    public void onStartDateSelect(View view) {
        editStart.show();
    }

    public void onEndDateSelect(View view) {
        editEnd.show();
    }

    public void onSaveClick(View view) {
        id = getIntent().getIntExtra("id", -1);
        title = editName.getText().toString();
        startDate = startDateButton.getText().toString();
        endDate = endDateButton.getText().toString();
        status = statusSpinner.getSelectedItem().toString();
        stringInstructor = instructorSpinner.getSelectedItem().toString();
        for (Instructor i : repository.getAllInstructors()) {
            if (stringInstructor.equals(i.toString())) {
                instructorId = i.getInstructorId();
            }
        }

        note = notes.getText().toString(); // optional
        if (termId == -1) {
            termId = getIntent().getIntExtra("termIdOnly", -1);
        }


        Term term = repository.getTerm(termId);
        String termStart = term.getStartDate();
        String termEnd = term.getEndDate();
        Date tStartDate = DateHelper.makeStringDate(termStart);
        Calendar tStartCalendar = Calendar.getInstance();
        tStartCalendar.setTime(tStartDate);

        Date tEndDate = DateHelper.makeStringDate(termEnd);
        Calendar tEndCalendar = Calendar.getInstance();
        tEndCalendar.setTime(tEndDate);

        int startYear = editStart.getDatePicker().getYear();
        int startMonth = editStart.getDatePicker().getMonth();
        int startDay = editStart.getDatePicker().getDayOfMonth();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);

        int endYear = editEnd.getDatePicker().getYear();
        int endMonth = editEnd.getDatePicker().getMonth();
        int endDay = editEnd.getDatePicker().getDayOfMonth();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);

        if (endCalendar.compareTo(startCalendar) < 0) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
        } else if (startCalendar.compareTo(tStartCalendar) < 0 || endCalendar.compareTo(tEndCalendar) > 0 || startCalendar.compareTo(tEndCalendar) > 0) {
            Toast.makeText(this, "Date must fall within term dates", Toast.LENGTH_SHORT).show();
        } else if (title.equals("") || startDate.equals("") || endDate.equals("") || status.equals("")) {
            Toast.makeText(this, "Make sure all fields are completed", Toast.LENGTH_SHORT).show();
        }
        //Some more date comparing logic to make sure data is within term
        else {
            if (id == -1) {
                Course course = new Course(0, title, startDate, endDate, note, status, termId, instructorId);
                repository.insert(course);
                Toast.makeText(this, "Course added", Toast.LENGTH_LONG).show();

            } else {
                Course course = new Course(id, title, startDate, endDate, note, status, termId, instructorId);
                repository.update(course);
                Toast.makeText(this, "Course updated", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    public void onAddClick(View view) {
        if (id == -1 || id == 0) {
            Toast.makeText(this, "Select a course first", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
            intent.putExtra("courseIdOnly", getIntent().getIntExtra("id", -1));
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        long today = new Date().getTime();
        switch (item.getItemId()) {
            case R.id.deleteCourse:
                id = getIntent().getIntExtra("id", -1);
                if (id != -1) {
                    Course courseToDelete = repository.getCourse(id);
                    for (Assessment a : repository.getAllAssessments()) {
                        if (a.getCourseId() == id) {
                            repository.delete(a);
                        }
                    }
                    repository.delete(courseToDelete);
                    Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "No term selected", Toast.LENGTH_SHORT).show();
                }
            case R.id.shareNote:
                note = notes.getText().toString();

                if (note.equals("")) {
                    Toast.makeText(this, "No notes to send for this course", Toast.LENGTH_LONG).show();
                } else {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, note);
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                }
            case R.id.courseStartNotify:
                if (id == -1) {
                    Toast.makeText(this, "Notification not set. Select an existing course first", Toast.LENGTH_LONG).show();
                } else {
                    String startDate = startDateButton.getText().toString();
                    Date myDate = DateHelper.makeStringDate(startDate);
                    Long startTrigger = myDate.getTime();

                    Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                    intent.putExtra("notification", editName.getText().toString() + ", starts today");
                    PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarManager.set(AlarmManager.RTC_WAKEUP, startTrigger, sender);
                    Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.courseEndNotify:
                if (id == -1) {
                    Toast.makeText(this, "Notification not set. Select an existing course first", Toast.LENGTH_LONG).show();
                } else {
                    String endDate = endDateButton.getText().toString();
                    Date myDate = DateHelper.makeStringDate(endDate);
                    Long startTrigger = myDate.getTime();

                    Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                    intent.putExtra("notification", editName.getText().toString() + ", ends today");
                    PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarManager.set(AlarmManager.RTC_WAKEUP, startTrigger, sender);
                    Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show();
                }

                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}