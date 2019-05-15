package com.evo.notes.db;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;


@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
@Entity(tableName = "note")
public class Note {

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.uid == newItem.uid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Note note = (Note) obj;
        return note.uid == this.uid && Objects.equals(note.textNote, this.textNote);
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "text")
    public String textNote;

    @ColumnInfo(name = "date_create")
    public Long dateCreateNote;

    @ColumnInfo(name = "date_update")
    public Long dateUpdateNote;
}
