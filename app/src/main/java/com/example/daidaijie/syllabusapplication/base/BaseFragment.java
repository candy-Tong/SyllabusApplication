package com.example.daidaijie.syllabusapplication.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu.LibraryFragment;

import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/10/12.
 */

public abstract class BaseFragment extends Fragment {

    protected static final String CLASS_NAME = LibraryFragment.class.getCanonicalName();

    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);

        init(savedInstanceState);

        return view;
    }

    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getContentView();


    protected void setupSwipeRefreshLayout(SwipeRefreshLayout mRefreshLayout) {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

}
