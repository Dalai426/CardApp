package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.ArrayList;

import model.word;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(@Nullable Context context) {
        super(context, database.DATABASE_NAME, null, database.VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="create table "+database.TABLE_NAME+"("+database.MWORD+" text, "+database.EWORD+" text,"+database.KEY_ID+" integer primary key autoincrement);";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String upgrade="drop table if exists "+database.TABLE_NAME+";";
        sqLiteDatabase.execSQL(upgrade);
    }

    public boolean insertData(word word){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(database.MWORD,word.getMword());
        values.put(database.EWORD,word.getEword());
        long result=db.insert(database.TABLE_NAME,null,values);
        db.close();
        if(result==-1){
            return false;
        }
        else {
            return true;
        }
    }



    public boolean updateData(word word){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(database.MWORD,word.getMword());
        values.put(database.EWORD,word.getEword());
        Cursor cursor=db.rawQuery("select * from "+database.TABLE_NAME+" where "+database.KEY_ID+"=?",new String[]{String.valueOf(word.getItemid())});
        if(cursor.getCount()>0){
            long result=db.update(database.TABLE_NAME,values,database.KEY_ID+"=?", new String[]{String.valueOf(word.getItemid())});
            db.close();
            if(result==-1){
                return false;
            }
            else{
                return true;
            }
        }else{
            db.close();
            return false;
        }

    }


    public boolean deleteData(word word){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+database.TABLE_NAME+" where "+database.KEY_ID+"=?",new String[]{String.valueOf(word.getItemid())});
        if(cursor.getCount()>0){
            long result=db.delete(database.TABLE_NAME, database.KEY_ID+"=?", new String[]{ String.valueOf(word.getItemid())});
            db.close();
            if(result==-1){
                return false;
            }
            else{
                return true;
            }
        }else{
            db.close();
            return false;
        }
    }

    public  Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+database.TABLE_NAME,null);
        db.close();
        return cursor;
    }
    public ArrayList<word> getWords(){
        ArrayList<word> words=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+database.TABLE_NAME,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                word w=new word();
                w.setMword(cursor.getString(0));
                w.setEword(cursor.getString(1));
                w.setItemid(cursor.getInt(2));
                words.add(w);
            }
        }else{
            db.close();
            return words;
        }
        db.close();
        return words;
    }
}
