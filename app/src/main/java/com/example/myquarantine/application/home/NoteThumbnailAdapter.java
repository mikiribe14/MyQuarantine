package com.example.myquarantine.application.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myquarantine.R;
import com.example.myquarantine.data.Note;
import java.util.List;

public class NoteThumbnailAdapter extends  RecyclerView.Adapter<NoteThumbnailAdapter.ViewHolder> {
    private List<Note> notes;
    final private OnListItemClickListener mOnListItemClickListener;
    private Context context;

    public NoteThumbnailAdapter (List<Note> noteList, OnListItemClickListener listener, Context contex){
        notes = noteList;
        mOnListItemClickListener = listener;
        context = contex;
    }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.n_thumbnail_item, parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(notes.get(position).getTitle());
            int day = notes.get(position).getDaySinceOutbreak();
            holder.day.setText(String.valueOf(day));

            if (notes.get(position).getStoredInCloud()){
                int color = ContextCompat.getColor(context, R.color.note_important);
                holder.itemView.setBackgroundColor(color);
            }

        }


        @Override
        public int getItemCount() {
            return notes.size();
        }

        public Note getItem (int position){
            return notes.get(position);
        }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                                                                        View.OnLongClickListener {
        TextView day;
        TextView title;

         ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
         }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mOnListItemClickListener.onLongListItemClick(getAdapterPosition());
            return true;
        }

    }
}
