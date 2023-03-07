package com.example.cardapp;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import data.DatabaseHandler;
import model.word;

public class FileAddd extends AppCompatActivity {

    ArrayList<word> words=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_addd);
    }
    public void add(View view){
        Intent i;
        i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri url=data.getData();
        System.out.println(url);
        BufferedReader reader;
        InputStream is = null;
        try {
            // filedaaa handaj ugch baina
            is = getContentResolver().openInputStream(url);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File нээгдэхэд алдаа гарлаа !!!", Toast.LENGTH_SHORT).show();
        }
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        try {
            DatabaseHandler db=new DatabaseHandler(this);
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                String[] ugs = line.split(",");
                word w=new word();
                w.setEword(ugs[2]);
                w.setMword(ugs[1]);
                w.setItemid(Integer.valueOf(ugs[0]));
                db.insertData(w);
            }
            db.close();
            Toast.makeText(this, "Амжилттай нэмэгдлэээ !!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Амжилтгүй !!!", Toast.LENGTH_SHORT).show();
        }
    }
}