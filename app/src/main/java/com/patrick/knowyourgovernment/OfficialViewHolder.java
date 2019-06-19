package com.patrick.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {
    public TextView possitionView;
    public TextView nameView;

    public OfficialViewHolder(View view) {
        super(view);
        possitionView= view.findViewById(R.id.titleTextView);
        nameView = view.findViewById(R.id.nameTextView);
    }

}
