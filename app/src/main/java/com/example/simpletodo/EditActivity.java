package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

// libraries for the functionality of the app
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    //create variable of views
    EditText etItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //reference views using their specific id
        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        //Add a title on the top when the user updates an item
        getSupportActionBar().setTitle("Edit item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // when the user is done editing, click to save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent which will pass the results
                Intent intent = new Intent();

                //pass the data
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set the result of the intent
                setResult(RESULT_OK, intent);

                //finish, close and go back
                finish();
            }
        });
    }
}