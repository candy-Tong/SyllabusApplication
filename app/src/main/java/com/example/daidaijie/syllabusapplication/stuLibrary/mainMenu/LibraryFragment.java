package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibItemAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends BaseFragment implements LibraryContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.libRecyclerView)
    RecyclerView mLibRecyclerView;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshLibLayout)
    SwipeRefreshLayout mRefreshLibLayout;

    private LibItemAdapter mLibItemAdapter;

    private static final String CLASS_NAME = LibraryFragment.class.getCanonicalName();

    private static final String EXTRA_POS = CLASS_NAME + ".mPosition";


    @Inject
    LibraryPresenter mLibraryPresenter;

    public static LibraryFragment newInstance(int position) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POS, position);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        DaggerLibraryComponent.builder()
                .libModelComponent(LibModelComponent.getInstance())
                .libraryModule(new LibraryModule(this,
                        args.getInt(EXTRA_POS, 0)))
                .build().inject(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLibItemAdapter = new LibItemAdapter(mActivity, null);
        mLibRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mLibRecyclerView.setAdapter(mLibItemAdapter);

        setupSwipeRefreshLayout(mRefreshLibLayout);
        mRefreshLibLayout.setOnRefreshListener(this);

        mRefreshLibLayout.post(new Runnable() {
            @Override
            public void run() {
                mLibraryPresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_library;
    }

    @Override
    public void onRefresh() {
        mLibraryPresenter.loadData();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mRefreshLibLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        if (mLibItemAdapter.getItemCount() == 0) {
            mEmptyTextView.setText("查找不到对应图书");
        }
        SnackbarUtil.ShortSnackbar(
                mLibRecyclerView, "获取失败", SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showData(List<LibraryBean> libraryBeen) {
        mLibItemAdapter.setLibraryBeen(libraryBeen);
        mLibItemAdapter.notifyDataSetChanged();
        if (libraryBeen.size() == 0) {
            showFailMessage("NULL");
        }
    }
}
