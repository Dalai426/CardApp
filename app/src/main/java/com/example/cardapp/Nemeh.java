package com.example.cardapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import data.Firebase;
import model.word;

public class Nemeh extends AppCompatActivity {

    DatabaseHandler db;
    EditText mongol;
    EditText english;
    int storage;

    SharedPreferences settingFile;
    Firebase fb;

    public static final String EXTRA_REPLY =
            "com.example.android.cardapp.extra.REPLY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nemeh);
        mongol=findViewById(R.id.mongol);
        english=findViewById(R.id.english);
        settingFile=getSharedPreferences("settingFile", Context.MODE_PRIVATE);

        storage=settingFile.getInt("storage",0);

        if(storage==0){
            db=new DatabaseHandler(this);
        }else{
            fb=new Firebase();
        }
    }
    public void oruulah(View view){
        word word=new word();
        try{
            word.setMword(String.valueOf(mongol.getText()));
            word.setEword(String.valueOf(english.getText()));
            if(storage==0){
                db.insertData(word);
            }else{
                fb.insertData(word);
            }
                mongol.setText("");
                english.setText("");
                Toast.makeText(this, "АМЖИЛТТАЙ!!!", Toast.LENGTH_LONG).show();

        }catch (Exception exception){
                Toast.makeText(this, "ХООСОН БАЙНА!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void bolih(View view){
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
        myAlertBuilder.setTitle("болих");
        myAlertBuilder.setMessage("Болихдоо итгэлтэй байна уу ?");

        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent replyIntent = new Intent();
                        finish();
                    }
                });

        myAlertBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // Create and show the AlertDialog.
        myAlertBuilder.show();
    }
}