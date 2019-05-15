package com.evo.notes.activity;

import com.evo.notes.RecyclerView.PaginationScrollListener;
import com.evo.notes.RecyclerView.RecyclerListener;
import com.evo.notes.adapter.NoteAdapter;

import com.evo.notes.adapter.NoteViewModel;
import com.evo.notes.db.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import com.evo.notes.R;


import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private NoteViewModel mNoteViewModel;
    private static final int NEW_NOTE_CODE = 1;
    private static final int UPDATE_NOTE_CODE = 2;
    public static final String EXTRA_NOTE_TEXT = "com.evo.notes.NOTE";
    public static final String EXTRA_NOTE_ID = "com.evo.notes.NOTE_ID";
    private Note mTempNote = null;
    public static boolean isSorted = true;
    private NoteAdapter adapter = null;

    //SwipeRefreshLayout swipeRefresh;


    private static final int PAGE_START = 0;
    public static int currentPage = PAGE_START;
    private final boolean isLastPage = false;
    public static final int totalPage = 10;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnItemTouchListener(new RecyclerListener(getApplicationContext(), recyclerView, new RecyclerListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mTempNote = mNoteViewModel.getById(position);
                Intent intent = new Intent(getApplicationContext(), NoteDetailsActivity.class);
                intent.putExtra(EXTRA_NOTE_TEXT, mTempNote.textNote);
                intent.putExtra(EXTRA_NOTE_ID, mTempNote.uid);
                startActivityForResult(intent, UPDATE_NOTE_CODE);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter = new NoteAdapter(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                adapter.refreshNotes();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        recyclerView.addItemDecoration(new com.evo.notes.RecyclerView.DividerItemDecoration(this));
        recyclerView.setAdapter(adapter);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);


        mNoteViewModel.getAllNotes().observe(this, notes -> adapter.setNotes(notes));
        //adapter.setNotes(list[0],PAGE_START,totalPage);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setLogo(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_toolbar_logo));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            mTempNote = new Note();
            mTempNote.dateCreateNote = new Date().getTime();
            mTempNote.dateUpdateNote = new Date().getTime();
            Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
            intent.putExtra(EXTRA_NOTE_ID, mTempNote.uid);
            startActivityForResult(intent, NEW_NOTE_CODE);
        });
        SearchView searchView = findViewById(R.id.my_search_viewer);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_NOTE_CODE:
                    switch (data.getStringExtra(NoteDetailsActivity.EXTRA_REPLY_ACTION)) {
                        case "remove":
                            NoteViewModel.remove(mTempNote.uid);
                            break;
                        case "update":
                            mTempNote.textNote = data.getStringExtra(NoteDetailsActivity.EXTRA_REPLY);
                            NoteViewModel.insert(mTempNote);
                            break;
                    }
                    break;
                case UPDATE_NOTE_CODE:
                    switch (data.getStringExtra(NoteDetailsActivity.EXTRA_REPLY_ACTION)) {
                        case "update":
                            mTempNote.textNote = data.getStringExtra(NoteDetailsActivity.EXTRA_REPLY);
                            mTempNote.dateUpdateNote = new Date().getTime();
                            NoteViewModel.update(mTempNote);
                            break;
                        case "remove":
                            NoteViewModel.remove(mTempNote.uid);
                            break;
                    }
                    break;
                default:
                    Toast.makeText(
                            getApplicationContext(),
                            "Заметка не сохранена",
                            Toast.LENGTH_LONG).show();
            }
        }
        adapter.setSort(isSorted);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:

                break;
            case R.id.action_sort:
                PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.my_toolbar), Gravity.RIGHT);

                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());


                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_sort_new_first:
                            isSorted = false;
                            break;
                        case R.id.action_sort_old_first:
                            isSorted = true;
                            break;
                        default:
                            isSorted = false;
                    }
                    isSorted = !isSorted;
                    adapter.setSort(isSorted);


                    return true;
                });

                popup.show(); //showing popup menu
                break;

        }

        return super.onOptionsItemSelected(menuItem);
    }
}
