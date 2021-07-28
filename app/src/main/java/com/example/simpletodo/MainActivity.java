/*
Name: Jobanpreet Singh
Submission Date: 07/27/2021
/Android Pre-work: SimpleTodo App
References Used:
https://courses.codepath.com/snippets/android_university/prework
*/

package com.example.simpletodo;

// libraries needed for the functionality of the app
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;  //vertical layout of the list
import androidx.recyclerview.widget.RecyclerView;         // modern way of showing a list

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;     // for warnings and to keep log
import android.view.View;
import android.widget.Button;          // buttons such as save and add
import android.widget.EditText;        // add text
import android.widget.Toast;           // small message to let the user know there item is added or updated

import org.apache.commons.io.FileUtils;          // used to save the list when app closed or list edited

import java.io.File;                        //reference files
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;                 // to build the list
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //keys for the edit activity and intent
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    //create the variables for the views and adapters
    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //declare a value for the variable using the id of each view
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        // this function will load the current items in the list at the start of app
        loadItems();

        // long press to delete the item from the list
        ItemsAdapter.onLongClickListener onLongClickListener =  new ItemsAdapter.onLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // delete the item form the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems(); // save the list after removing the intended item
            }
        };

        ItemsAdapter. onClickListener onClickListener = new ItemsAdapter.onClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single clicked at position" + position);
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };


        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // add an new item to the bottom of the list
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //add the item to the model
                items.add(todoItem);
                //notify the adapter the item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable /*@org.jetbrains.annotations.NotNull*/ Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // get updated text
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //get the original position of the edited text from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the correct position
            items.set(position, itemText);

            //notify the adapter
            itemsAdapter.notifyItemChanged(position);

            //save the changes
            saveItems();

            //leave a message for the user
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            //warning
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //error
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing them in the data file

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //error
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}