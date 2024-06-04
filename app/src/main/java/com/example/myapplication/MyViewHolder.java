package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageButton moreButton;
    Button startBtn;
    TextView nameView, distanceView;
    Context context;

    public MyViewHolder(@NonNull View itemView, rvInterface rvInterface) {
        super(itemView);
        moreButton = itemView.findViewById(R.id.moreBtn);
        startBtn = itemView.findViewById(R.id.button2);
        nameView = itemView.findViewById(R.id.name);
        distanceView = itemView.findViewById(R.id.distance);



        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (rvInterface != null){

                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){

                        rvInterface.longClick(pos);
                    }
                }

                return true;
            }
        });
    }
}
