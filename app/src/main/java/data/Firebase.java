package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cardapp.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.word;

public class Firebase {


    ArrayList<word> words;
    DatabaseReference fbase;


    public Firebase(){
        words=new ArrayList<>();
        fbase=FirebaseDatabase.getInstance().getReference().child("words");
    }

    public boolean insertData(word word){
        fbase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               word.setItemid((int)dataSnapshot.getChildrenCount()+1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fbase.push().setValue(word);
        return true;
    }



    public boolean updateData(word word){
        System.out.println("---------------------------------------------------------");
       Map<String,Object> map=new HashMap<>();
       map.put("eword",word.getEword());
       map.put("itemid",word.getItemid());
       map.put("mword",word.getMword());


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    fbase.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists() && (snapshot.getValue(word.class).getMword().compareTo(word.getMword())==0
                                    || snapshot.getValue(word.class).getEword().compareTo(word.getEword())==0)
                                    && snapshot.getValue(word.class).getItemid()==word.getItemid() ){

                                fbase.child(key).child("eword").setValue(word.getEword());
                                fbase.child(key).child("itemid").setValue(word.getItemid());
                                fbase.child(key).child("mword").setValue(word.getMword());

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        fbase.addListenerForSingleValueEvent(eventListener);


        return  true;
    }


    public boolean deleteData(word word){

        Query applesQuery = fbase.orderByChild("eword").equalTo(word.getEword());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    if(appleSnapshot.getValue(word.class).getMword().compareTo(word.getMword())==0){
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("amjiltgvi delete");
            }
        });



        return  true;
    }


    public ArrayList<word> getWords(){

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    DatabaseReference reference=fbase.child(key);
                    reference.child("itemid").setValue(i++);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                words.add(snapshot.getValue(word.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        fbase.addListenerForSingleValueEvent(eventListener);
        return words;
    }
}
