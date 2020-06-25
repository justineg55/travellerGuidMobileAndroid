package com.fbanseptcours.travellerguidmobileandroid.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.model.Result;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter <CardViewAdapter.ResultViewHolder> {

    List<Result> results;

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView activity;
        TextView budget;


        public ResultViewHolder(View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.txt_activity);
            budget = itemView.findViewById(R.id.txt_budget);
        }
    }
    public CardViewAdapter(List<Result> results) {
        this.results = results;
    }


    @NonNull
    @Override
    public CardViewAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result, parent, false);
        ResultViewHolder ingredientViewHolder = new ResultViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapter.ResultViewHolder holder, int position) {
//        Result ingredient = results.get(position);
//        holder.activity.setText(ingredient.getActivity());
//        holder.budget.setText(ingredient.getBudget());

        TextView textViewNameActivity=holder.activity;
        TextView textViewBudget=holder.budget;

        textViewNameActivity.setText(results.get(position).getActivity());
        textViewBudget.setText(results.get(position).getBudget());

    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
