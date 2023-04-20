package com.wgu.courseschedulerc196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Assessment;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Instructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseDetails extends AppCompatActivity {

    private EditText editName;
    private DatePickerDialog editStart;
    private DatePickerDialog editEnd;
    private Button startDateButton;
    private Button endDateButton;


    private Spinner statusSpinner;
    private Spinner instructorSpinner;
    private EditText notes;

    private Repository repository = new Repository(getApplication());

    private int id;
    private String title;
    private String startDate;
    private String endDate;
    private List<String> statusTypes = new ArrayList<>();
    private String status;
    private List<Instructor> listInstructors;
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
        ArrayAdapter<String> statusArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusTypes);
        statusSpinner.setAdapter(statusArrayAdapter);
        status = getIntent().getStringExtra("status");

        listInstructors = new ArrayList<>();
        listInstructors.addAll(repository.getAllInstructors());

        instructorId = getIntent().getIntExtra("instructorId", -1);
        instructor = repository.getInstructor(instructorId);
        instructorSpinner = findViewById(R.id.instructorSpinner);
        ArrayAdapter<Instructor> instructorArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listInstructors);
        instructorSpinner.setAdapter(instructorArrayAdapter);

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

        ArrayAdapter<Instructor> instructorArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listInstructors);
        instructorSpinner.setAdapter(instructorArrayAdapter);
        int instructorPosition = instructorArrayAdapter.getPosition(instructor);
        instructorSpinner.setSelection(instructorPosition);

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
        instructor = (Instructor) instructorSpinner.getSelectedItem();
        instructorId = instructor.getInstructorId();

        note = notes.getText().toString(); // optional
        if (termId == -1){
            termId = getIntent().getIntExtra("termIdOnly", -1);
        }
        else{
            termId = getIntent().getIntExtra("termId", -1);
        }


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
            Toast.makeText(this, "End Date must be after start date", Toast.LENGTH_LONG).show();
        }
        else if (title.equals("") || startDate.equals("") || endDate.equals("") || status.equals("")){
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
        if(id == -1 || id == 0){
            Toast.makeText(this, "Select a course first", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
            intent.putExtra("courseIdOnly",getIntent().getIntExtra("id", -1));
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCourse:
                id = getIntent().getIntExtra("id", -1);
                if (id != -1) {
                    Course courseToDelete = repository.getCourse(id);
                    for(Assessment a : repository.getAllAssessments()){
                        if(a.getCourseId() == id){
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

                if(note.equals("")){
                    Toast.makeText(this, "No notes to send for this course", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, note);
                    shareIntent.setType("text/plain");
                    startActivity(shareIntent);
                }


        }
        return super.onOptionsItemSelected(item);
    }
}