package com.abdo.calculator;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.Holder> {


    Set<String> list = new HashSet<>();

    public void setList(Set<String> list) {
        this.list = list;
    }




    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history , parent , false);

        return new HistoryRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        List<String> a = new ArrayList<>();
        a.addAll(list);

        holder.txt.setText(a.get(position));

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView txt;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);






    }
    }



}
