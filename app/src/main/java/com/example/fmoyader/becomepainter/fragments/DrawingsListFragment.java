package com.example.fmoyader.becomepainter.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.adapters.DrawingsListRecyclerViewAdapter;
import com.example.fmoyader.becomepainter.services.DeletePaintingService;
import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPaintingListListener}
 * interface.
 */
public class DrawingsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnPaintingListListener mListener;
    private static final int ID_DRAWING_LOADER = 10001;

    private RecyclerView drawingListRecylerView;
    private DrawingsListRecyclerViewAdapter drawingsListRecyclerViewAdapter;

    public DrawingsListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drawingListRecylerView = (RecyclerView) inflater.inflate(R.layout.fragment_drawings_list, container, false);

        drawingListRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        drawingsListRecyclerViewAdapter = new DrawingsListRecyclerViewAdapter(getContext(), mListener);
        drawingListRecylerView.setAdapter(drawingsListRecyclerViewAdapter);

        ItemTouchHelper swipeToDeleteToucher = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        String paintingId = (String) viewHolder.itemView.getTag();
                        Intent intentToDeletePaintingService = new Intent(getActivity(), DeletePaintingService.class);
                        intentToDeletePaintingService.putExtra(DeletePaintingService.EXTRA_PAINTING_ID, paintingId);
                        getActivity().startService(intentToDeletePaintingService);
                    }
                }
        );

        swipeToDeleteToucher.attachToRecyclerView(drawingListRecylerView);

        return drawingListRecylerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportLoaderManager()
                .restartLoader(ID_DRAWING_LOADER, null, this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaintingListListener) {
            mListener = (OnPaintingListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPaintingListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                PaintingContract.PaintingEntry.CONTENT_URI, null, null, null,
                "_ID DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        drawingsListRecyclerViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPaintingListListener {
        void onPaintingItemClick(String paintingId);
    }
}
