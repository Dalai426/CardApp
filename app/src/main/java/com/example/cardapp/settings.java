package com.example.cardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    SharedPreferences settingsfile;
    int mode;
    int storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsfile=getSharedPreferences("settingFile", Context.MODE_PRIVATE);
        mode=settingsfile.getInt("mode",0);
        storage=settingsfile.getInt("storage",0);
        System.out.println("mode: "+mode);

        switch (mode){
            case 1:
                ((RadioButton)findViewById(R.id.radioButton1)).setChecked(true);
                break;
            case 2:
                ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);
                break;
            default:
                ((RadioButton)findViewById(R.id.radioButton3)).setChecked(true);
                break;
        }
        switch (storage){
            case 1:
                ((RadioButton)findViewById(R.id.radioButton5)).setChecked(true);
                break;
            default:
                ((RadioButton)findViewById(R.id.radioButton4)).setChecked(true);
                break;
        }
    }

    public void rb(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioButton1:
                if(checked)
                    mode=1;
                break;
            case R.id.radioButton2:
                if(checked)
                    mode=2;
                    break;
            case R.id.radioButton3:
                if(checked)
                    mode=0;
                    break;
        }
    }
    public void store(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radioButton4:
                if(checked)
                    storage=0;
                break;
            case R.id.radioButton5:
                if(checked)
                    storage=1;
                break;
        }
    }
    public void save(View view){
        SharedPreferences.Editor editor=settingsfile.edit();
        editor.putInt("mode",mode);
        editor.putInt("storage",storage);
        editor.apply();
        Toast.makeText(this, "Mode "+mode, Toast.LENGTH_SHORT).show();
    }
    public void back(View view){
        finish();
    }
}