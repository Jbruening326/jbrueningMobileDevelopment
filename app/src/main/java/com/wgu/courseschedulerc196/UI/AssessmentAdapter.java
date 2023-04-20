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
import com.wgu.courseschedulerc196.entities.Assessment;

import java.util.List;


public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder>{

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private final TextView assessmentTitleTextView;



        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentTitleTextView = itemView.findViewById(R.id.assessmentTitleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);


                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("type", current.getAssessmentType());
                    intent.putExtra("end", current.getEndDate());
                    intent.putExtra("courseId", current.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;

    private final LayoutInflater mInflator;

    public AssessmentAdapter(Context context){
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }


    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if(mAssessments != null){
            Assessment current = mAssessments.get(position);
            String title = current.getTitle();
            String endDate = current.getEndDate();

            holder.assessmentTitleTextView.setText(title);
        }
        else{
            holder.assessmentTitleTextView.setText("No name");
        }

    }

    @Override
    public int getItemCount() {
        if (mAssessments != null){
            return mAssessments.size();
        }
        return 0;
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}