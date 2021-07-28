package com.example.simpletodo;

//import all of needed libraries
import android.view.LayoutInflater; // to inflate the view
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;  // view the text

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;   // for the use recycler view list on the main page

import java.util.List; // to list the data

//Responsible for displaying the data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    //interfaces to collect the position of the item using click listeners
    public interface onClickListener {
        void onItemClicked(int position);
    }
    public interface onLongClickListener {
        void onItemLongClicked(int position);
    }


    List<String> items; // list the items
    onLongClickListener longClickListener;  // to delete the items
    onClickListener clickListener;          // to edit the items

    //constructor
    public ItemsAdapter(List<String> items, onLongClickListener longClickListener, onClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    //@org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull /*@org.jetbrains.annotations.NotNull*/ ViewGroup parent, int viewType) {

        // use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    // binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull /*@org.jetbrains.annotations.NotNull*/ ItemsAdapter.ViewHolder holder, int position) {
        // grab the item at the position
        String item = items.get(position);
        //Bind the item into the specified view holder
        holder.bind(item);
    }

    // tells the rv how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //create a view holder
    //container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        public ViewHolder(@NonNull /*@org.jetbrains.annotations.NotNull*/ View itemView) {
            super(itemView);
            tvItem= itemView.findViewById(android.R.id.text1);
        }

        //update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was Long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
