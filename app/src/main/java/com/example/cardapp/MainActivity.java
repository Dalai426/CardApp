package com.example.cardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.DatabaseHandler;
import data.Firebase;
import model.word;

public class MainActivity extends AppCompatActivity {


    public ArrayList<word> words;
    SharedPreferences settingFile;
    TextView mongol, english;

    DatabaseHandler db;
    Firebase fb;
    int currentindex;

    int mode;
    int storage;

    int update=-1;
    int night_day;


    long maxid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        english=findViewById(R.id.english);
        mongol=findViewById(R.id.mongol);
        currentindex=0;

        // settings medeelel hadgalah file vvsgeh
        settingFile=getSharedPreferences("settingFile", Context.MODE_PRIVATE);

        if(settingFile.getInt("mode",-1)==-1){
            SharedPreferences.Editor edit=settingFile.edit();
            edit.putInt("mode",0);
            edit.commit();
        }else{
            mode=settingFile.getInt("mode",0);
            System.out.println("mode"+mode);
        }

        if(settingFile.getInt("currentword",-1)==-1){
            SharedPreferences.Editor edit=settingFile.edit();
            edit.putInt("currentword",currentindex);
            edit.commit();
        }else{
            currentindex=settingFile.getInt("currentword",0);
            System.out.println("shared : "+settingFile.getInt("currentword",0));
        }
        if(settingFile.getInt("storage",-1)==-1){
            SharedPreferences.Editor edit=settingFile.edit();
            edit.putInt("storage",0);
            edit.commit();
        }else{
            storage=settingFile.getInt("storage",0);
            System.out.println("storing technique : "+settingFile.getInt("storage",0));
        }

        if(settingFile.getInt("night",-1)==-1){
            SharedPreferences.Editor edit=settingFile.edit();
            edit.putInt("night",0);
            edit.commit();
        }else{
            night_day=settingFile.getInt("night",0);
        }

        if (night_day == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }




        if(storage==0){
            db=new DatabaseHandler(this);
            words=db.getWords();
        }else{
            try {
                fb = new Firebase();
                words = fb.getWords();
                System.out.println("on create words size is" + words.size());
            }catch (Exception exception){
                storage=0;
                db=new DatabaseHandler(this);
                words=db.getWords();
            }
        }

        if(words.size()>0){
            displayWords();
        }else{
            emptyWords();
        }


        mongol.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                update=1;
                shinechleh(view);
                return true;
            }
        });
        english.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                update=0;
                shinechleh(view);
                return true;
            }
        });

    }


    public void displayWords(){
        if(currentindex>words.size()-1 || currentindex<0){
            currentindex=0;
        }
        if(mode==0){
            mongol.setText(words.get(currentindex).getMword().toLowerCase());
            english.setText(words.get(currentindex).getEword().toLowerCase());
        }
        else if(mode==1){
            mongol.setText("");
            english.setText(words.get(currentindex).getEword().toLowerCase());
        }else{
            mongol.setText(words.get(currentindex).getMword().toLowerCase());
            english.setText("");
        }


        Toast.makeText(this, String.valueOf(currentindex+1), Toast.LENGTH_SHORT).show();
    }



    public void emptyWords(){
        mongol.setText("Хоосон !!!");
        english.setText("Хоосон !!!");
        currentindex=0;
    }
   public void daraah(View view){
        if(words.size()==0){
            emptyWords();
            return;
        }
        currentindex++;
        if(words.size()-1<currentindex){
            currentindex=words.size()-1;
        }
        displayWords();
   }
   public void omnoh(View view){
       if(words.size()==0){
           emptyWords();
           return;
       }
       currentindex--;
       if(0>currentindex){
           currentindex=0;
       }
       displayWords();
   }
   public void nemeh(View view){
        Intent intent=new Intent(this, Nemeh.class);
        startActivity(intent);
   }

   public void ustgah(View view){
       if (words.size()>0){
           AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
           myAlertBuilder.setTitle("УСТГАХ");
           myAlertBuilder.setMessage(words.get(currentindex).getEword()+" , "+words.get(currentindex).getMword()+" үгийг устгахдаа итгэлтэй байна уу ?");

           // Add the dialog buttons.
           myAlertBuilder.setPositiveButton("OK",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           if(storage==0){
                                db.deleteData(words.get(currentindex));
                           }else{
                               try {
                                   fb.deleteData(words.get(currentindex));
                               }catch (Exception exception){
                                   Toast.makeText(MainActivity.this, "Уучлаарай устгаж чадсангүй!!!", Toast.LENGTH_SHORT).show();
                               }

                           }

                           words.remove(currentindex);
                           if(words.size()>0) {
                               displayWords();
                           }else{
                               emptyWords();
                           }
                       }
                   });

           myAlertBuilder.setNegativeButton("Cancel",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });

           myAlertBuilder.show();
       }
   }






   public void shinechleh(View view){
        if (words.size()>0) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.modal_dialog);
            final EditText en = dialog.findViewById(R.id.etEnglish);
            final EditText mn = dialog.findViewById(R.id.etmongol);
            RadioGroup rb = dialog.findViewById(R.id.rb);
            RadioButton rb1 = dialog.findViewById(R.id.mongol_hel);
            RadioButton rb2 = dialog.findViewById(R.id.angli_hel);


            if(update==-1){
                rb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        en.setText(words.get(currentindex).getEword());
                    }
                });
                rb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mn.setText(words.get(currentindex).getMword());
                    }
                });
            }else if(update==0){
                rb2.setChecked(true);
                mn.setText(words.get(currentindex).getMword());
            }else{
                rb1.setChecked(true);
                en.setText(words.get(currentindex).getEword());
            }


            Button btn = dialog.findViewById(R.id.button);
            btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if(String.valueOf(en.getText()).replaceAll(" ","").length()!=0 && String.valueOf(mn.getText()).replaceAll(" ","").length()!=0){
                                               word w = new word();
                                               w.setEword(String.valueOf(en.getText()));
                                               w.setMword(String.valueOf(mn.getText()));
                                               w.setItemid(words.get(currentindex).getItemid());


                                                   if (true == (storage==0?db.updateData(w):fb.updateData(w))){
                                                       Toast.makeText(MainActivity.this, "Амжилттай", Toast.LENGTH_SHORT).show();
                                                   } else {
                                                       Toast.makeText(MainActivity.this, "Амжилтгүй", Toast.LENGTH_SHORT).show();
                                                   }

                                               words.get(currentindex).setEword(w.getEword());
                                               words.get(currentindex).setMword(w.getMword());
                                               displayWords();
                                               dialog.cancel();
                                           }else{
                                               Toast.makeText(MainActivity.this, "Аль нэг талбар хоосон байна", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }
            );
            dialog.show();

        }
   }





    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor=settingFile.edit();
        editor.putInt("currentword",currentindex);
        editor.putInt("storage",storage);
        editor.putInt("night",night_day);
        editor.apply();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.nightmode).setTitle("day mode");
        } else{
            menu.findItem(R.id.nightmode).setTitle("night mode");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this,settings.class);
                startActivity(intent);
                return true;
            case R.id.addfile:
                Intent i=new Intent(MainActivity.this, FileAddd.class);
                startActivity(i);
                return true;
            case R.id.nightmode:
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                // Set the theme mode for the restarted activity.
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    night_day=0;
                } else {
                    AppCompatDelegate.setDefaultNightMode
                            (AppCompatDelegate.MODE_NIGHT_YES);
                    night_day=1;
                }
                // Recreate the activity for the theme change to take effect.
                recreate();
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

}