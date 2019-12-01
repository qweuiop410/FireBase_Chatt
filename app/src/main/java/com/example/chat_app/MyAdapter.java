package com.example.chat_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    public String name="";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public  View view;

        public  LinearLayout linearLayout_view;
        public  TextView textView_name;
        public  TextView textView_msg;

        public MyViewHolder(View v) {
            super(v);
            view = v;

            linearLayout_view = v.findViewById(R.id.LinearLayout_view);
            textView_name = v.findViewById(R.id.TextView_nickname);
            textView_msg = v.findViewById(R.id.TextView_msg);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ChatData> myDataset,String getName) {
        mDataset = myDataset;
        name = getName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        ChatData chat = mDataset.get(position);

        holder.textView_name.setText(chat.getName());
        holder.textView_msg.setText(chat.getMsg());

        if (name.equals(chat.getName()))
        {
            holder.textView_msg.setBackgroundResource(R.drawable.msg_panel_my);
            holder.linearLayout_view.setGravity(Gravity.RIGHT);
        }
        else
        {
            holder.textView_msg.setBackgroundResource(R.drawable.msg_panel);
            holder.linearLayout_view.setGravity(Gravity.LEFT);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  mDataset==null? 0 : mDataset.size();
    }

    public  ChatData getDate(int position) {return mDataset != null?mDataset.get(position):null;}

    public  void addChat(ChatData chat)
    {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size() - 1);
    }
}