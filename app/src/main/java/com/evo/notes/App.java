package com.evo.notes;

import android.app.Application;


import com.evo.notes.db.NotesDatabase;

import static androidx.room.Room.databaseBuilder;

public class App extends Application {
    private static App instance;

    private NotesDatabase db;

    public App() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = databaseBuilder(this, NotesDatabase.class, "db_app")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public NotesDatabase getDatabase() {
        return db;
    }
}
