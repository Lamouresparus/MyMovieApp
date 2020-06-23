package com.example.mymovieapp.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();

    private static final String DATABASE_NAME = "Movie DataBase";

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(TAG,"Creating movie database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, AppDatabase.DATABASE_NAME).build();

            }
        }
        Log.d(TAG,"Retriving database");
        return sInstance;
    }


    public abstract FavouriteDao favouriteDao();

}
