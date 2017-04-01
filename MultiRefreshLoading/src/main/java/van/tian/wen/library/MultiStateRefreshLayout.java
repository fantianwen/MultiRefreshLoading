package van.tian.wen.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import van.tian.wen.library.adapter.BasicRecyclerAdapter;
import van.tian.wen.library.view.MultiStateView;

public class MultiStateRefreshLayout extends SwipeRefreshLayout {

    private RelativeLayout mRelativeLayout;
    private boolean mUseRecyclerView;
    private FrameLayout mOuterContainer;
    private ListView mListView;

    private RecyclerView mRecyclerView;

    private ScrollView mScrollView;

    private View mEmptyView;
    private View mRequestErrorView;

    private View mLoadingView;
    private View mErrorView;
    private View mSuccessView;
    private View mUnknownView;

    private MultiStateView mFootView;

    private ListView mDefaultListView;

    private View mDefaultLoadingView;
    private View mDefaultSuccessView;
    private View mDefaultErrorView;
    private View mDefaultUnknownView;
    private View mDefaultEmptyView;
    private View mDefaultRequestErrorView;

    private Context mContext;

    private float mFirstTouchY;
    private float mLastTouchY;
    private OnLoadingListener mOnLoadingListener;
    private boolean isLoading;
    private int mTouchSlop;
    private LinearLayoutManager mLinearLayoutManager;

    public MultiStateRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiStateRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        initDefaultViews();

        initRes(attrs);

        mOuterContainer = new FrameLayout(mContext);
        FrameLayout.LayoutParams outerLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mOuterContainer.setLayoutParams(outerLayoutParams);

        mFootView = new MultiStateView(mContext);

        FrameLayout.LayoutParams recyclerFootLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        if (useRecyclerView()) {
            mFootView.setLayoutParams(recyclerFootLayoutParams);
        }

        mFootView.setLoadingView(mLoadingView)
                .setErrorView(mErrorView)
                .setSuccessView(mSuccessView)
                .setUnknownView(mUnknownView);

        mScrollView = new ScrollView(mContext);
        mScrollView.setFillViewport(true);
        FrameLayout.LayoutParams scrollLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(scrollLayoutParams);

        mRelativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.setLayoutParams(relativeLayoutParams);

        mRelativeLayout.addView(mEmptyView);
        mRelativeLayout.addView(mRequestErrorView);

        mScrollView.addView(mRelativeLayout);

        if (mUseRecyclerView) {
            mOuterContainer.addView(mRecyclerView, mRecyclerView.getLayoutParams());
        } else {
            mOuterContainer.addView(mListView, mListView.getLayoutParams());
        }

        mOuterContainer.addView(mScrollView, mScrollView.getLayoutParams());
        this.addView(mOuterContainer, mOuterContainer.getLayoutParams());

        this.mScrollView.setVisibility(View.INVISIBLE);

        if (this.mUseRecyclerView) {
            if (mRecyclerView != null) {
                setRecyclerView(mRecyclerView);
            }
        } else {
            if (mListView != null) {
                setListView(mListView);
            }
        }

//        // no need
//        if (mUseRecyclerView) {
//            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
//            if (adapter instanceof BasicRecyclerAdapter) {
//                BasicRecyclerAdapter basicRecyclerAdapter = (BasicRecyclerAdapter) adapter;
//                basicRecyclerAdapter.setFootView(mFootView);
//            } else {
//                throw new IllegalArgumentException("the adapter you set to RecyclerView should be an extension of BasicRecyclerAdapter");
//            }
//        } else {
//            mListView.addFooterView(mFootView);
//        }

        if (!useRecyclerView()) {
            if (mListView != null) {
                mListView.addFooterView(mFootView);
            }
        }

        if (mUnknownView != null) {
            mUnknownView.setClickable(false);
        }

    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (canRefresh()) {
                            setEnabled(true);
                        } else {
                            setEnabled(false);
                        }
                        if (canLoadMore(newState)) {
                            loadData();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initDefaultViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mDefaultRequestErrorView = layoutInflater.inflate(R.layout.view_default_request_error, this, false);
    }

    private void initRes(AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateRefreshLayout);
        int listViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_listView, -1);
        if (listViewId > -1) {
            View listView = layoutInflater.inflate(listViewId, this, false);
            if (listView instanceof AbsListView) {
                mListView = (ListView) listView;
            } else {
                throw new IllegalArgumentException("illegal type of AbsListView");
            }
        }

        int recyclerViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_recyclerView, -1);
        if (recyclerViewId > -1) {
            View recyclerView = layoutInflater.inflate(recyclerViewId, this, false);
            if (recyclerView instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) recyclerView;
            } else {
                throw new IllegalArgumentException("illegal type of RecyclerView");
            }
        }

        mUseRecyclerView = typedArray.getBoolean(R.styleable.MultiStateRefreshLayout_useRecyclerView, false);

        int emptyViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_emptyView, -1);
        if (emptyViewId > -1) {
            mEmptyView = layoutInflater.inflate(emptyViewId, this, false);
        } else {
            mEmptyView = mDefaultEmptyView;
        }

        int loadingViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footLoadingView, -1);
        if (loadingViewId > -1) {
            mLoadingView = layoutInflater.inflate(loadingViewId, this, false);
        } else {
            mLoadingView = mDefaultLoadingView;
        }

        int errorViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footErrorView, -1);
        if (errorViewId > -1) {
            mErrorView = layoutInflater.inflate(errorViewId, this, false);
        } else {
            mErrorView = mDefaultErrorView;
        }

        int successViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footSuccessView, -1);
        if (successViewId > -1) {
            mSuccessView = layoutInflater.inflate(successViewId, this, false);
        } else {
            mSuccessView = mDefaultSuccessView;
        }

        int unknownViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_footUnknownView, -1);
        if (unknownViewId > -1) {
            mUnknownView = layoutInflater.inflate(unknownViewId, this, false);
        } else {
            mUnknownView = mDefaultUnknownView;
        }

        int requestErrorViewId = typedArray.getResourceId(R.styleable.MultiStateRefreshLayout_requestErrorView, -1);
        if (requestErrorViewId > -1) {
            mRequestErrorView = layoutInflater.inflate(requestErrorViewId, this, false);
        } else {
            mRequestErrorView = mDefaultRequestErrorView;
        }

        typedArray.recycle();
    }

    public void setListView(final ListView mListView) {
        this.mListView = mListView;

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        if (canRefresh()) {
                            setEnabled(true);
                        } else {
                            setEnabled(false);
                        }
                        if (canLoadMore(scrollState)) {
                            loadData();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private boolean canRefresh() {
        return isTop();
    }

    private boolean isTop() {
        if (useRecyclerView()) {
            if (mLinearLayoutManager != null && mLinearLayoutManager.getChildCount() > 0) {
                View firstView = mLinearLayoutManager.getChildAt(0);
                if (firstView.getPaddingTop() <= mRecyclerView.getPaddingTop()) {
                    return true;
                }
            }
        } else {
            if (mListView.getCount() > 0) {
                if (mListView.getFirstVisiblePosition() == 0
                        && mListView.getChildAt(0).getTop() >= mListView.getTop()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //Fix for support lib bug, happening when onDestroy is
            return true;
        }
    }

    private void loadData() {
        if (mOnLoadingListener != null) {
            setLoading(true);
        }
    }

    public void setLoading(boolean shouldLoad) {
        if (useRecyclerView()) {
            if (mRecyclerView == null) return;
        } else {
            if (mListView == null) return;
        }

        isLoading = shouldLoad;
        if (isLoading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
            showLoadingFoot();
            mOnLoadingListener.onLoadMore();
            isLoading = false;
        } else {
            mFirstTouchY = 0;
            mLastTouchY = 0;
        }
    }

    public void setOnLoadingListener(OnLoadingListener loadingListener) {
        this.mOnLoadingListener = loadingListener;
    }

    private boolean canLoadMore(int scrollState) {
        if (isBottom() && !isLoading && isPullingUp() && scrollState == 0) {
            addFootView();
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY = event.getRawY();
                    break;

                case MotionEvent.ACTION_UP:
                    mLastTouchY = event.getRawY();
                    break;

                default:
                    break;
            }
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    private boolean isPullingUp() {
        return (mFirstTouchY - mLastTouchY) >= mTouchSlop;
    }

    private boolean isBottom() {
        if (useRecyclerView()) {
            if (mRecyclerView != null && mLinearLayoutManager.getChildCount() > 0) {

                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                if (adapter instanceof BasicRecyclerAdapter) {
                    BasicRecyclerAdapter basicRecyclerAdapter = (BasicRecyclerAdapter) adapter;

                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                    int itemThreadHold = 0;
                    if (basicRecyclerAdapter.getFootView() != null && basicRecyclerAdapter.getHeaderView() != null) {
                        itemThreadHold = 2;
                    } else {
                        if (basicRecyclerAdapter.getHeaderView() != null) {
                            itemThreadHold = 1;
                        } else if (basicRecyclerAdapter.getFootView() != null) {
                            itemThreadHold = 1;
                        } else {
                            itemThreadHold = 0;
                        }
                    }
                    if (lastVisibleItem + itemThreadHold + 1 >= totalItemCount) {
                        return true;
                    }
                }
            }
        } else {
            if (mListView.getCount() > 0) {
                if (mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1 ||
                        mListView.getLastVisiblePosition() == mListView.getAdapter().getCount()
                    /*&& mListView.getChildAt(mListView.getChildCount() - 1).getBottom() <= mListView.getHeight() + CommonUtil.dip2px(mContext, 12)*/) {
                    return true;
                }
            }
        }

        return false;
    }

    public void showEndLoadFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_UNKNOWN);
    }

    public void setSuccessFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_SUCCESS);
    }

    public void setLoadingFootView() {
        mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_LOADING);
    }

    public void showErrorFootView() {
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_ERROR);
    }

    public void showFootView(int viewState) {
        switch (viewState) {
            case MultiStateView.VIEW_STATE_UNKNOWN:
                showEndLoadFootView();
                break;
            case MultiStateView.VIEW_STATE_ERROR:
                showErrorFootView();
                break;
            case MultiStateView.VIEW_STATE_LOADING:
                showLoadingFoot();
                break;
            case MultiStateView.VIEW_STATE_SUCCESS:
                showSuccessFoot();
                break;
            default:
                break;
        }
    }

    public void showErrorMsg() {
        this.mListView.setVisibility(View.INVISIBLE);
        this.mScrollView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.GONE);
        this.mRequestErrorView.setVisibility(View.VISIBLE);
    }

    public void setFootNoMoreText(String footNoMoreText) {
        if (this.mEmptyView instanceof TextView) {
            ((TextView) mEmptyView).setText(footNoMoreText);
        }
    }

    public boolean useRecyclerView() {
        return mUseRecyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public View getFootView() {
        return mFootView;
    }

    public interface OnLoadingListener {
        void onLoadMore();
    }

    public void showEmptyView() {
        this.mListView.setVisibility(View.INVISIBLE);
        this.mScrollView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.VISIBLE);
        this.mRequestErrorView.setVisibility(View.GONE);
    }

    public void showNormalView() {
        this.mListView.setVisibility(View.VISIBLE);
        this.mScrollView.setVisibility(View.GONE);
    }

    public void showLoadingFoot() {
        this.mEmptyView.setVisibility(View.GONE);
        this.mScrollView.setVisibility(View.GONE);
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_LOADING);
    }

    private void addFootView() {

        if (useRecyclerView()) {
            if (mRecyclerView != null) {
                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                if (adapter instanceof BasicRecyclerAdapter) {
                    BasicRecyclerAdapter baseRecyclerAdapter = (BasicRecyclerAdapter) adapter;
                    View footView = baseRecyclerAdapter.getFootView();
                    if (footView == null) {
                        baseRecyclerAdapter.setFootView(mFootView);
                    }
                } else {
                    throw new IllegalArgumentException("this recyclerView should be adapted with a extension of BasicRecyclerAdapter");
                }
            }
        } else {
            if (mListView != null) {
                if (mListView.getFooterViewsCount() == 0) {
                    mListView.addFooterView(mFootView);
                }
            }
        }

    }

    private void removeFootView() {
        if (mListView != null) {
            if (mListView.getFooterViewsCount() > 0) {
                mListView.removeFooterView(mFootView);
            }
        }
    }

    public void showSuccessFoot() {
        this.mEmptyView.setVisibility(View.GONE);
        this.mScrollView.setVisibility(View.GONE);
        this.mFootView.setViewStateAndValidate(MultiStateView.VIEW_STATE_SUCCESS);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setOnSucessFootClickListener(OnClickListener listener) {
        this.mFootView.getSuccessView().setOnClickListener(listener);
    }

    public void setOnErrorFootClickListener(OnClickListener listener) {
        this.mFootView.getErrorView().setOnClickListener(listener);
    }

    public View getErrorView() {
        return mFootView.getErrorView();
    }

    public View getSuccessView() {
        return mFootView.getSuccessView();
    }
}
