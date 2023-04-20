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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wgu.courseschedulerc196.Helper.DateHelper;
import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.database.Repository;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetails extends AppCompatActivity {
    private DatePickerDialog start;
    private DatePickerDialog end;
    private Button startDateButton;
    private Button endDateButton;
    private EditText termName;


    int termId;
    private String termTitle;
    private String startDate;
    private String endDate;

    int numCourses;
    Term currentTerm;
    Repository repository = new Repository(getApplication());
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);


        termId = getIntent().getIntExtra("id", -1);
        termName = findViewById(R.id.editTextTermName);
        termTitle = getIntent().getStringExtra("title");

        startDateButton = findViewById(R.id.startDateButton);
        start = DateHelper.initDatePicker(startDateButton);
        startDate = getIntent().getStringExtra("start");
        //put in on resume


        endDateButton = findViewById(R.id.endDateButton);
        end = DateHelper.initDatePicker(endDateButton);
        endDate = getIntent().getStringExtra("end");
        //put in on resume

        recyclerView = findViewById(R.id.courseRecyclerView);//stays on create
        //on resume

    }

    @Override
    protected void onResume() {
        super.onResume();
        termName.setText(termTitle);
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for(Course c: repository.getAllCourses()){
            if(c.getTermId() == termId){
                filteredCourses.add(c);
            }
        }
        courseAdapter.setCourses(filteredCourses);

    }

    public void onStartDateSelect(View view) {
        start.show();
    }

    public void onEndDateSelect(View view) {
        end.show();
    }


    public void onSaveClick(View view) {

        termId = getIntent().getIntExtra("id", -1);
        termTitle = termName.getText().toString();
        startDate = startDateButton.getText().toString();
        endDate = endDateButton.getText().toString();


        int startYear = start.getDatePicker().getYear();
        int startMonth = start.getDatePicker().getMonth();
        int startDay = start.getDatePicker().getDayOfMonth();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startYear, startMonth, startDay);

        int endYear = end.getDatePicker().getYear();
        int endMonth = end.getDatePicker().getMonth();
        int endDay = end.getDatePicker().getDayOfMonth();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endYear, endMonth, endDay);

        if(endCalendar.compareTo(startCalendar) < 0 ) {
            Toast.makeText(this, "End Date must be after start date", Toast.LENGTH_LONG).show();
        }
        else if(termTitle.equals("") || startDate.equals("") || endDate.equals("")){
            Toast.makeText(this, "Make sure all fields are completed", Toast.LENGTH_SHORT).show();
        }
        else {
            if(termId == -1){
                Term term = new Term(0, termName.getText().toString(),startDateButton.getText().toString() , endDateButton.getText().toString());
                repository.insert(term);
                Toast.makeText(this, "Term added", Toast.LENGTH_LONG).show();
            }
            else{
                Term term = new Term(termId, termName.getText().toString(), startDateButton.getText().toString(), endDateButton.getText().toString());
                repository.update(term);
                Toast.makeText(this, "Term updated", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(TermDetails.this, TermList.class);
            startActivity(intent);
        }

    }
    public void onAddClick(View view) {
        if(termId == -1 || termId == 0){
            Toast.makeText(this, "Select a term first", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Intent intent = new Intent(TermDetails.this, CourseDetails.class);
            //always send course id. the recycler views sends all course data, but add button sends nothing
            intent.putExtra("termIdOnly",getIntent().getIntExtra("id", -1));
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_details_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteTerm:
                termId = getIntent().getIntExtra("id", -1);
                if(termId != -1){
                    Term termToDelete = repository.getTerm(termId);
                    List<Course> associatedCourses = new ArrayList<>();
                    for(Course c : repository.getAllCourses()) {
                        if(termId == c.getTermId()){
                            associatedCourses.add(c);
                        }
                    }
                    if(associatedCourses.size() == 0){
                        repository.delete(termToDelete);
                        Toast.makeText(this, "Term Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TermDetails.this, TermList.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "Can't Delete. Remove all courses first", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "No term selected", Toast.LENGTH_SHORT).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

}