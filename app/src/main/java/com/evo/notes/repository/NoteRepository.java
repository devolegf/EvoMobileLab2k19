package com.evo.notes.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.evo.notes.App;
import com.evo.notes.db.Note;
import com.evo.notes.db.NoteDao;
import com.evo.notes.db.NotesDatabase;

import java.util.List;


public class NoteRepository {
    private final NoteDao mNoteDao;
    private final LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        NotesDatabase db = App.getInstance().getDatabase();
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(2)
                        .setPageSize(5).build();

        LiveData<PagedList<Note>> mAllNotesPaged = new LivePagedListBuilder<>(
                mNoteDao.getAllNotesPaged(), pagedListConfig).build();
    }


    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public Note getById(int id) {
        return mNoteDao.findById(id);
        //return new getByIdAsyncTask(mNoteDao).execute(id);
    }

    public void insert(Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note) {
        new updateAsyncTask(mNoteDao).execute(note);
    }

    public void remove(int id) {
        new removeAsyncTask(mNoteDao).execute(id);
    }

    private static class getAllAsyncTask extends AsyncTask<Integer, Void, List<Note>> {
        private final NoteDao mAsyncTaskDao;

        getAllAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Note> doInBackground(final Integer... params) {
            return mAsyncTaskDao.getAll(params[0], params[1]);

        }


    }

    private static class removeAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final NoteDao mAsyncTaskDao;

        removeAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.remove(params[0]);

            return null;
        }


    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {

        private final NoteDao mAsyncTaskDao;

        updateAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private final NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
