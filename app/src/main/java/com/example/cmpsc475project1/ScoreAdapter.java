package com.example.cmpsc475project1;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

//public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
//
//    private List<ScoreEntity> scores;
//    public LayoutInflater layoutInflater;
//
//    public ScoreAdapter(Context context) {
//        layoutInflater = LayoutInflater.from(context);
//    }
//
//    public void setScores(List<ScoreEntity> scores) {
//        this.scores = scores;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        return new ScoreViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
//        if (scores != null) {
//            ScoreEntity score = scores.get(position);
//            holder.nameTextView.setText(score.name);
//            holder.scoreTextView.setText(String.valueOf(score.score));
//        }
//        else {
//            // Covers the case of data not being ready yet.
//            holder.nameTextView.setText("...loading...");
//
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (scores != null)
//            return scores.size();
//        else
//            return 0;
//    }
//
//    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
//        TextView nameTextView;
//        TextView scoreTextView;
//        private ScoreEntity score;
//
//        public ScoreViewHolder(@NonNull View itemView) {
//            super(itemView);
//            nameTextView = itemView.findViewById(R.id.listItemName);
//            scoreTextView = itemView.findViewById(R.id.listItemNumber);
//
//        }
//    }
//}

