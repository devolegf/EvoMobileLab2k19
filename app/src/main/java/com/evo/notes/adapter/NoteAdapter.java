package com.evo.notes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evo.notes.R;
import com.evo.notes.db.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.evo.notes.activity.MainActivity.currentPage;
import static com.evo.notes.activity.MainActivity.isSorted;
import static com.evo.notes.activity.MainActivity.totalPage;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {
    private List<Note> mNotes;
    private List<Note> mNotesFiltered;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    @SuppressWarnings("unused")
    public NoteAdapter(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        //this.mNotes = postItems;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new NoteAdapter.NoteViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
            case VIEW_TYPE_LOADING:
                //return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (mNotesFiltered != null) {
            Note current = mNotesFiltered.get(position);
            holder.noteItemViewText.setText(current.textNote);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

            if (current.dateUpdateNote == null) current.dateUpdateNote = new Date().getTime();

            String formattedDate = dateFormat.format(current.dateUpdateNote);
            holder.noteItemViewDate.setText(dateFormat.format(current.dateUpdateNote));
            dateFormat = new SimpleDateFormat("HH:mm");
            holder.noteItemViewTime.setText(dateFormat.format(current.dateUpdateNote));
        } else {
            // Covers the case of data not being ready yet.
            holder.noteItemViewText.setText("У вас нет заметок");
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean isLoaderVisible = false;
        if (isLoaderVisible) {
            return position == getItemCount() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.trim().isEmpty()) {
                    mNotesFiltered = mNotes;
                } else {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : mNotes) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.textNote.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mNotesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mNotesFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mNotesFiltered = (ArrayList<Note>) results.values;
                //mNotes = mNotesFiltered;
                //setNotes(mNotesFiltered);
                notifyDataSetChanged();
            }
        };
    }

    public void setSort(boolean isSorted) {
        if (isSorted) {
            Collections.sort(this.mNotesFiltered,
                    (o1, o2) -> o1.dateUpdateNote.compareTo(o2.dateUpdateNote));
            Collections.reverse(this.mNotesFiltered);
        } else {
            Collections.sort(this.mNotesFiltered,
                    (o1, o2) -> o1.dateUpdateNote.compareTo(o2.dateUpdateNote));
        }
        notifyDataSetChanged();
    }


    public void refreshNotes() {
        setNotes(mNotes);
    }

    public void setNotes(List<Note> notes) {
        //notes.stream().skip(offset).limit(limit).collect(Collectors.toList());
        mNotes = notes;
        if (currentPage == 0) {

            mNotesFiltered = notes.stream().skip(currentPage * totalPage).limit(totalPage).collect(Collectors.toList());
        } else {
            //if (mNotesFiltered == null) mNotesFiltered = mNotes;
            if ((mNotesFiltered.size() < mNotes.size()) && (mNotesFiltered != null)) {
                if ((mNotes.size() - mNotesFiltered.size()) > totalPage) {
                    mNotesFiltered.addAll(mNotes.stream().skip(currentPage * totalPage).limit(totalPage).collect(Collectors.toList()));// = notes;
                } else {
                    mNotesFiltered.addAll(mNotes.stream().skip(mNotesFiltered.size()).limit((mNotes.size() - mNotesFiltered.size())).collect(Collectors.toList()));// = notes;
                }

            } else {
                mNotesFiltered = mNotes;
            }

        }
        this.setSort(isSorted);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNotesFiltered != null)
            return mNotesFiltered.size();
        else return 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteItemViewText;
        private final TextView noteItemViewDate;
        private final TextView noteItemViewTime;

        private NoteViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            noteItemViewText = itemView.findViewById(R.id.textView);
            noteItemViewDate = itemView.findViewById(R.id.textViewDate);
            noteItemViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

}
