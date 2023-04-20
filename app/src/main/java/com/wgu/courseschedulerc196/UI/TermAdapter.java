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
import com.wgu.courseschedulerc196.entities.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.termAdapterView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Term current = mTerms.get(position);
                    Intent intent = new Intent(context, TermDetails.class);

                    intent.putExtra("id", current.getTermId());
                    intent.putExtra("title", current.getTermTitle());
                    intent.putExtra("start", current.getStartDate());
                    intent.putExtra("end", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflator;
    public TermAdapter(Context context){
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }



    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms != null){
            Term current = mTerms.get(position);
            String name = current.getTermTitle();
            holder.termItemView.setText(name);
        }
        else {
            holder.termItemView.setText("Add a term by selecting the add button");
        }
    }

    @Override
    public int getItemCount() {
        if (mTerms != null){
            return mTerms.size();
        }
        return 0;
    }

    public void setTerms(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }

}

