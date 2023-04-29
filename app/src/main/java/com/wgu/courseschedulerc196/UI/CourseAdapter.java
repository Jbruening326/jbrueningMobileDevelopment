package com.wgu.courseschedulerc196.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.courseschedulerc196.R;
import com.wgu.courseschedulerc196.entities.Course;
import com.wgu.courseschedulerc196.entities.Term;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseItemView;



        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.courseAdapterView);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetails.class);

                    intent.putExtra("id", current.getCourseId());
                    intent.putExtra("title", current.getCourseTitle());
                    intent.putExtra("start", current.getStartDate());
                    intent.putExtra("end", current.getEndDate());
                    intent.putExtra("note", current.getNote());
                    intent.putExtra("status", current.getStatus());
                    intent.putExtra("name", current.getInstructorName());
                    intent.putExtra("phone", current.getInstructorPhone());
                    intent.putExtra("email", current.getInstructorEmail());
                    intent.putExtra("termId", current.getTermId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;

    private final LayoutInflater mInflator;

    public CourseAdapter(Context context){
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }


    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.course_list_item, parent, false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses != null){
            Course current = mCourses.get(position);
            String name = current.getCourseTitle();
            String startDate = current.getStartDate();
            String endDate = current.getEndDate();
            holder.courseItemView.setText(name);

        }
        else{
            holder.courseItemView.setText("No name");

        }

    }

    @Override
    public int getItemCount() {
        if (mCourses != null){
            return mCourses.size();
        }
        return 0;
    }

    public void setCourses(List<Course> courses){
        mCourses = courses;
        notifyDataSetChanged();
    }
}


