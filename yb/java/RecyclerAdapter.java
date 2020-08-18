package com.example.myapplication;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] tempNames;
    private String[] tempDetails;
    private String[] names = {"노경", "유빈", "채원", "병희"};
    private String[] detail = {"디테일 1", "디테일 2", "디테일 3", "디테일 4"};
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profileImage;
        public TextView name;
        public TextView detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView2);
            name = (TextView)itemView.findViewById(R.id.name);
            detail = (TextView)itemView.findViewById(R.id.detail);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardvieww, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(tempNames[position]);
        holder.detail.setText(tempDetails[position]);
        holder.profileImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeString(){
        //DataManager DM = new DataManager();
        tempNames = new String[4];
        tempDetails = new String[4];
        tempNames[0] = "노경";
        tempNames[1] = "유빈";
        tempNames[2] = "채원";
        tempNames[3] = "병희";

        tempDetails[0] = "22:00  >>> 24:00";
        tempDetails[1] = "22:00  >>> 24:00";
        tempDetails[2] = "22:00  >>> 24:00";
        tempDetails[3] = "22:00  >>> 24:00";
    }
}
