package com.example.fmoyader.becomepainter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.fragments.DrawingsListFragment.OnPaintingListListener;
import com.example.fmoyader.becomepainter.utils.SQLiteUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DrawingsListRecyclerViewAdapter extends RecyclerView.Adapter<DrawingsListRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private Cursor cursor;
    private final OnPaintingListListener mListener;

    public DrawingsListRecyclerViewAdapter(Context context, OnPaintingListListener listener) {
        this.context = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawing_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            Painting painting = SQLiteUtils.mapCursor2Painting(cursor);
            holder.bind(painting);
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_painting_author)
        TextView authorTextView;
        @BindView(R.id.text_view_painting_title)
        TextView titleTextView;
        @BindView(R.id.text_view_painting_description)
        TextView descriptionTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        public void bind(Painting painting) {
            authorTextView.setText(context.getString(R.string.prefix_author) + " " + painting.getAuthor());
            titleTextView.setText(painting.getTitle());

            if (painting.getDescription() == null || painting.getDescription().trim().isEmpty()) {
                descriptionTextView.setVisibility(View.GONE);
            } else {
                descriptionTextView.setText(painting.getDescription());
            }

            itemView.setTag(painting.getId() + "");
        }

        @Override
        public void onClick(View v) {
            mListener.onPaintingItemClick(((String) itemView.getTag()));
        }
    }
}
