package van.tian.wen.library;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import van.tian.wen.library.adapter.BasicAdapter;
import van.tian.wen.library.adapter.BasicRecyclerAdapter;
import van.tian.wen.library.model.Pageable;
import van.tian.wen.library.util.NetWorkUtil;

public abstract class BaseAbstractRefreshLoadingFragment<Pagination extends Pageable, T> extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        MultiStateRefreshLayout.OnLoadingListener,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        BasicRecyclerAdapter.OnItemClickListener,
        BasicRecyclerAdapter.OnItemLongClickListener {

    protected Context mContext;

    private MultiStateRefreshLayout mRefreshLayout;
    protected ListView mListView;
    protected RecyclerView mRecyclerView;

    private BasicAdapter<T> mAdapter;
    private BasicRecyclerAdapter<T> mRecyclerViewAdapter;

    protected ArrayList<T> mList = new ArrayList<>();

    protected int pageNo = 1;
    protected boolean isLastPage;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
            this.mContext = activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(provideLayoutResId(), container, false);
        initViews(view);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadingListener(this);
        mRefreshLayout.setOnErrorFootClickListener(this);
        mRefreshLayout.setOnSucessFootClickListener(this);

        if (!mRefreshLayout.useRecyclerView()) {
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);
        }

        return view;
    }

    protected abstract int provideLayoutResId();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRefreshLayout.useRecyclerView()) {
            BasicRecyclerAdapter basicRecyclerAdapter = recyclerViewAdapter();
            mRecyclerView.setAdapter(basicRecyclerAdapter);
            setRecyclerFoot(basicRecyclerAdapter);
        } else {
            mListView.setAdapter(listViewAdapter());
        }
    }

    private BasicRecyclerAdapter recyclerViewAdapter() {
        mRecyclerViewAdapter = setRecyclerViewAdapter();

        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.setOnItemClickListener(this);
            mRecyclerViewAdapter.setOnItemLongClickListener(this);
        }

        mRecyclerViewAdapter.setList(mList, true);
        return mRecyclerViewAdapter;
    }

    protected BasicRecyclerAdapter<T> setRecyclerViewAdapter() {
        return null;
    }

    private BaseAdapter listViewAdapter() {
        mAdapter = setAdapter();
        if (mAdapter == null) {
            mAdapter = new BasicAdapter<T>(mContext) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return getAdapterView(position, convertView, parent);
                }
            };
        }
        mAdapter.setList(mList, true);
        return mAdapter;
    }

    protected BasicAdapter<T> setAdapter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();

        request();

    }

    private void request() {
        setRefreshing(true);
        checkNetWork();

        onRequestData();
    }

    protected abstract void onRequestData();

    protected void showList(List<T> pageList) {

        if (pageList != null && pageList.size() > 0) {
            mRefreshLayout.showNormalView();

            if (mRefreshLayout.useRecyclerView()) {
                mRecyclerViewAdapter.clear();
                mRecyclerViewAdapter.addExtra(pageList);
                mRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                mAdapter.clear();
                mAdapter.addExtraList(pageList);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mRefreshLayout.showEmptyView();
        }
    }

    protected void addExtra(List<T> extra) {
        if (extra != null && extra.size() > 0) {
            if (mRefreshLayout.useRecyclerView()) {
                mRecyclerViewAdapter.addExtra(extra);
                mRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                mAdapter.addExtraList(extra);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void checkNetWork() {
        if (!NetWorkUtil.isNetworkAvailable(mContext)) {
            setRefreshing(false);
            mRefreshLayout.showErrorFootView();
            return;
        }
    }

    private void setRefreshing(final boolean shouldRefresh) {

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(shouldRefresh);
            }
        });

    }

    protected void initViews(View view) {

        mRefreshLayout = (MultiStateRefreshLayout) view.findViewById(R.id.validSaleRefreshLayout);
        mRefreshLayout.setFootNoMoreText(emptyText());

        int listViewId = provideListViewResId();

        if (mRefreshLayout.useRecyclerView()) {
            if (listViewId <= 0) {
                mRecyclerView = mRefreshLayout.getRecyclerView();
            } else {
                mRecyclerView = (RecyclerView) buildViewFromRes(listViewId);
                mRefreshLayout.setRecyclerView(mRecyclerView);
            }
        } else {
            if (listViewId <= 0) {
                mListView = mRefreshLayout.getListView();
            } else {
                mListView = (ListView) buildViewFromRes(listViewId);
                mRefreshLayout.setListView(mListView);
            }
            ViewCompat.setNestedScrollingEnabled(mListView, true);
        }
    }

    private View buildViewFromRes(int listViewId) {
        View inflatedView = LayoutInflater.from(mContext).inflate(listViewId, null);
        if (inflatedView instanceof ListView) {
            return (ListView) inflatedView;
        } else if (inflatedView instanceof RecyclerView) {
            return (RecyclerView) inflatedView;
        } else {
            throw new InflateException("you should put the root view with 'ListView' tag");
        }
    }

    /**
     * provide a listView resId if you want,absolutely,we have a default one
     *
     * @return
     */
    protected abstract int provideListViewResId();

    /**
     * onRefresh
     */
    @Override
    public void onRefresh() {
        resetPageNo();
        request();
    }

    /**
     * onLoadingMore
     */
    @Override
    public void onLoadMore() {
        checkNetWork();
        onRequestMore();
    }

    @Override
    public void onClick(View v) {
        if (v == mRefreshLayout.getErrorView()) {
            // onClick Error Foot View
            request();
        } else if (v == mRefreshLayout.getSuccessView()) {
            // onClick Success Foot View
            request();
        } else {
            // TODO for more conditions
        }
    }

    private void syncState(Pagination body) {
        isLastPage = ((body.getTotalPage() == pageNo) || body.isLastPage());
    }

    /*====================== API ======================================*/

    protected void setEmptyViewText(String text) {
        mRefreshLayout.setFootNoMoreText(text);
    }

    protected String emptyText() {
        return "没有有效房源";
    }

    protected void onRequestMore() {
        if (!isLastPage) {
            pageNo++;
            mRefreshLayout.showLoadingFoot();
            request();
        } else {
            mRefreshLayout.showEndLoadFootView();
        }
    }

    protected abstract View getAdapterView(int position, View convertView, ViewGroup parent);

    protected void onRequestResult(Pagination body) {
        setRefreshing(false);

        if (body != null) {
            syncState(body);
            if (isLastPage) {
                mRefreshLayout.showEndLoadFootView();
            } else {
                mRefreshLayout.showSuccessFoot();
            }

            if (pageNo == 1) {
                showList(body.getPageList());
            } else {
                addExtra(body.getPageList());
            }

        } else {
            mRefreshLayout.showEmptyView();
        }
    }

    protected void dismissLoading() {
        setRefreshing(false);
    }

    protected void onRequestResultFail() {
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.showErrorMsg();
    }

    protected void setListHead(View headView) {
        if (headView == null) {
            throw new NullPointerException();
        }

        if (mRefreshLayout.useRecyclerView()) {
            if (mRecyclerViewAdapter != null) {
                mRecyclerViewAdapter.setHeaderView(headView);
            }
        } else {
            if (mListView != null) {
                mListView.addHeaderView(headView);
            }
        }
    }

    protected void setListHead(int layoutResId) {
        if (layoutResId == 0) {
            throw new IllegalArgumentException("layoutResId should exist");
        }
        setListHead(View.inflate(mContext, layoutResId, null));
    }

    protected void setRecyclerFoot(BasicRecyclerAdapter recyclerAdapter) {
        if (mRecyclerView != null) {
            if (recyclerAdapter.getFootView() == null) {
                recyclerAdapter.setFootView(mRefreshLayout.getFootView());
            }
        }
    }

    /*============================================================*/

    private void resetPageNo() {
        pageNo = 1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int positionWithoutHead = position - getListViewHeadCount();
        if (positionWithoutHead >= 0 && positionWithoutHead < mAdapter.getCount()) {
            onItemClicked(parent, view, positionWithoutHead, id);
        }
    }

    private int getListViewHeadCount() {
        return mListView.getHeaderViewsCount();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int positionWithoutHead = position - getListViewHeadCount();

        if (positionWithoutHead >= 0 && positionWithoutHead < mAdapter.getCount()) {
            onItemLongClicked(parent, view, positionWithoutHead, id);
        }
        return true;
    }

    protected void onItemLongClicked(AdapterView<?> parent, View view, int position, long id) {

    }

    protected void onItemClicked(AdapterView<?> parent, View view, int position, long id) {

    }

    // recyclerview's adapter
    @Override
    public void onItemClicked(View view, int position) {

    }

    // recyclerview's adapter
    @Override
    public void onItemLongClicked(View view, int position) {

    }
}
