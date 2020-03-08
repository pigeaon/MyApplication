package com.example.myapplication.frags.main;


import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.app.Fragment;
import com.example.common.widget.GalleryView;
import com.example.myapplication.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment {

    @BindView(R.id.galleryView)
    GalleryView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData() {
        super.initData();
        mGalley.setup(getLoaderManager(), new GalleryView.SelectChangeListener() {
            @Override
            public void onSelectCountChange(int count) {

            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

}
