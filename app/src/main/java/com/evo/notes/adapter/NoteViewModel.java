package com.evo.notes.adapter;

import android.app.Application;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.paging.PagedList;

import com.evo.notes.db.Note;

import com.evo.notes.repository.NoteRepository;


import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private static NoteRepository mRepository;
    private final LiveData<List<Note>> mAllNotes;
    private LiveData<PagedList<Note>> mAllNotesPaged;


    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
        //mAllNotesPaged = mRepository.getAllNotesPaged();
    }

    public LiveData<PagedList<Note>> getNotesPaged() {
        return mAllNotesPaged;
    }


    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public Note getById(int id) {
        return mAllNotes.getValue().get(id);
    }

    public static void remove(int id) {

        mRepository.remove(id);
    }

    public static void update(Note note) {
        mRepository.update(note);
    }

    public static void insert(Note note) {
        mRepository.insert(note);
    }

}
