package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {
    Context context;

    List<Item> Items;

    private final rvInterface rvInterface;


    public MyAdaptor(Context context, List<Item> Items, rvInterface rvInterface) {
        this.context = context;
        this.Items = Items;
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(view, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nameView.setText(Items.get(position).getName());
        holder.distanceView.setText(Items.get(position).getDistance());

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, holder.getAdapterPosition());
            }
        });

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(context, Items.get(position));
            }
        });
    }

    private void showPopupMenu(View v, int pos) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                long itemId = item.getItemId();
                if (itemId == R.id.menu_duplicate) {
                    // Handle duplicate action
                    DBHelper DB1 = new DBHelper(context);
                    String name = Items.get(pos).getName();
                    Item item1 = new Item(name + "*", Items.get(pos).getDistance(),
                            Items.get(pos).getId() + 1, Items.get(pos).getLatitude(), Items.get(pos).getLongitude());
                    DB1.insertItem(item1);

                    Items.add(pos+1,item1);

                    notifyItemInserted(pos + 1);

                    Toast.makeText(context, "Duplicate clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    DBHelper DB1 = new DBHelper(context);
                    long id = Items.get(pos).getId();
                    DB1.deleteItem(id);

                    Items.remove(pos);

                    notifyItemRemoved(pos);

                    Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_edit) {

                    CostumeDialog customDialog = new CostumeDialog(context, Items.get(pos) ,pos); // 'this' refers to the current activity
                    customDialog.show();

                    Toast.makeText(context, "Edit clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();

    }


    @Override
    public int getItemCount() {
        return Items.size();
    }

    private void startService(Context context, Item item) {
        Intent intent = new Intent(context, ActiveTracking.class);

        intent.putExtra("name", item.getName());
        intent.putExtra("COORDINATED_TAG", item.getLatitude() + "," + item.getLongitude());
        intent.putExtra("DISTANCE_TAG", item.getParseDistance());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}

