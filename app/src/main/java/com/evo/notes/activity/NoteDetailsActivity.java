package com.evo.notes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.evo.notes.R;


public class NoteDetailsActivity extends AppCompatActivity {

    private EditText mEditNoteView;
    private boolean mChanged = false;
    public static final String EXTRA_REPLY =
            "com.evo.notes.REPLY";
    public static final String EXTRA_REPLY_ACTION =
            "com.evo.notes.ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Intent intent = getIntent();
        String message =
                intent.getStringExtra(MainActivity.EXTRA_NOTE_TEXT);
        int mNoteId = intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1);

        mEditNoteView = findViewById(R.id.editNote);
        mEditNoteView.setText(message);
        mEditNoteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mChanged = true;
                //invalidateOptionsMenu();
            }
        });
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Edit Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem item = menu.findItem(R.id.action_remove);
        /*if (mChanged) {
            item.setVisible(true);
        } else { item.setVisible(false);}
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent replyIntent = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                if (TextUtils.isEmpty(mEditNoteView.getText())) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Текст заметки не может быть пустым",
                            Toast.LENGTH_LONG).show();
                } else {
                    String text = mEditNoteView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, text);
                    replyIntent.putExtra(EXTRA_REPLY_ACTION, "update");
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }

                break;
            case R.id.action_remove:
                replyIntent.putExtra(EXTRA_REPLY_ACTION, "remove");
                replyIntent.putExtra(EXTRA_REPLY, "");
                setResult(RESULT_OK, replyIntent);
                finish();
                break;
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mEditNoteView.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
