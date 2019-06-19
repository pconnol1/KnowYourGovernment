package com.patrick.knowyourgovernment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OfficialsAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private static final String TAG = "OfficialsAdapter";
    private List<Official> officialList;
    private MainActivity mainAct;

    public OfficialsAdapter(List<Official> officialList, MainActivity mainAct){
        this.officialList=officialList;
        this.mainAct=mainAct;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official official = officialList.get(position);
        holder.nameView.setText(official.getName() + "  ( " + official.getParty() + " )");
        holder.possitionView.setText(official.getTitle());
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
