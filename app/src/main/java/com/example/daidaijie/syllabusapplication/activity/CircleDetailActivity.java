package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.CommentAdapter;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.service.CircleCommentsService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CircleDetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.commentRecyclerView)
    RecyclerView mCommentRecyclerView;

    RecyclerView mContentRecyclerView;
    @BindView(R.id.commentEditext)
    EditText mCommentEditext;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;
    private CirclesAdapter mCirclesAdapter;

    private CommentAdapter mCommentAdapter;

    private List<PostListBean> mPostListBeen;

    private PostListBean mPostListBean;

    private static final String EXTRA_POST_BEAN =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PostBean";

    private static final String EXTRA_PHOTO_WIDTH =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PhotoWidth";

    int mVisibleHeight;
    boolean mIsKeyboardShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }
        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight();
            }
        });


        mPostListBean = (PostListBean) getIntent().getSerializableExtra(EXTRA_POST_BEAN);

        mPostListBeen = new ArrayList<>();
        mPostListBeen.add(mPostListBean);

        mCirclesAdapter = new CirclesAdapter(this, mPostListBeen,
                getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));
        //以后一定要记住这句话
        mContentRecyclerView = new RecyclerView(this);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mCirclesAdapter);

        mCommentAdapter = new CommentAdapter(this, null);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentAdapter.setHeaderView(mContentRecyclerView);
        mCommentRecyclerView.setAdapter(mCommentAdapter);

        getComment();


    }

    private void getKeyboardHeight() {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);

        int visibleHeight = r.height();

        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }

        if (mVisibleHeight == visibleHeight) {
            return;
        }

        mVisibleHeight = visibleHeight;

        int mRootHeight = mRootView.getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRootHeight -= getStatusBarHeight();
        }

//        Toast.makeText(CircleDetailActivity.this, "height" + mVisibleHeight + " " + mRootHeight, Toast.LENGTH_SHORT).show();
        // Magic is here
        if (mVisibleHeight != mRootHeight) {
            mIsKeyboardShow = true;
        } else {
            mIsKeyboardShow = false;
        }
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mCommentEditext.getLayoutParams();
        layoutParams.bottomMargin = mRootHeight - mVisibleHeight;
        mRootView.requestLayout();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_circle_detail;
    }

    public static Intent getIntent(Context context, PostListBean postBean, int photoWidth) {
        Intent intent = new Intent(context, CircleDetailActivity.class);
        intent.putExtra(EXTRA_POST_BEAN, postBean);
        intent.putExtra(EXTRA_PHOTO_WIDTH, photoWidth);
        return intent;
    }


    private void getComment() {
        Retrofit retrofit = RetrofitUtil.getDefault();
        CircleCommentsService commentsService = retrofit.create(CircleCommentsService.class);
        commentsService.get_comments(mPostListBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CommentInfo commentInfo) {
                        mCommentAdapter.setCommentInfo(commentInfo);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                });
    }

}
