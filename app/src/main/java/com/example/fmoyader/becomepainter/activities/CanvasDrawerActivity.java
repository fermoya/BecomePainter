package com.example.fmoyader.becomepainter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.fragments.CanvasFragment;
import com.example.fmoyader.becomepainter.fragments.DrawingsListFragment;
import com.example.fmoyader.becomepainter.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanvasDrawerActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener, DrawingsListFragment.OnPaintingListListener {

    public static final String EXTRA_SEE_DRAWINGS = "extra_see_drawings";
    @BindView(R.id.drawer_left_menu_options)
    ListView drawerMenuOptionsListView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    public static final String EXTRA_PAINTING_ID = "extra_painting_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_drawer);
        ButterKnife.bind(this);

        drawerMenuOptionsListView.setOnItemClickListener(this);

        Fragment fragment = new CanvasFragment();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_PAINTING_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_PAINTING_ID, intent.getStringExtra(EXTRA_PAINTING_ID));
                fragment.setArguments(bundle);
            } else if (intent.hasExtra(EXTRA_SEE_DRAWINGS)){
                fragment = new DrawingsListFragment();
            }
        }

        swapContentFrame(fragment);

        drawerMenuOptionsListView.setItemChecked(1, true);
        drawerLayout.closeDrawer(drawerMenuOptionsListView);
    }

    private void swapContentFrame(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment;
        switch (position) {
            case 0:
                Log.d(getString(R.string.tag_drawer), "Home section selected on the drawer");
                fragment = new CanvasFragment();
                break;
            case 1:
                Log.d(getString(R.string.tag_drawer), "My Drawings section selected on the drawer");
                fragment = new DrawingsListFragment();
                break;
            case 2:
                Log.d(getString(R.string.tag_drawer), "Settings section selected on the drawer");
                fragment = new SettingsFragment();
                break;
            default:
                throw new IllegalArgumentException("There is no drawer menu option for posiition " +  position);
        }

        swapContentFrame(fragment);

        drawerMenuOptionsListView.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerMenuOptionsListView);
    }

    @Override
    public void onPaintingItemClick(String paintingId) {
        Log.d(getString(R.string.tag_drawer), "Painting " + paintingId + " clicked");
        Fragment fragment = new CanvasFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PAINTING_ID, paintingId);
        fragment.setArguments(bundle);
        swapContentFrame(fragment);
    }
}
