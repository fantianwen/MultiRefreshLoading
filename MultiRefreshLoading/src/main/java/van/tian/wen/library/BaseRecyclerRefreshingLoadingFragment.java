package van.tian.wen.library;

import android.view.View;
import android.view.ViewGroup;

import van.tian.wen.library.adapter.BasicRecyclerAdapter;
import van.tian.wen.library.model.Pageable;

public abstract class BaseRecyclerRefreshingLoadingFragment<Pagination extends Pageable, T> extends BaseAbstractRefreshLoadingFragment<Pagination, T> {

    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_base_recycler_impl;
    }

    @Override
    protected void onRequestData() {

    }

    @Override
    protected int provideListViewResId() {
        return 0;
    }

    @Override
    protected View getAdapterView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    protected BasicRecyclerAdapter<T> setRecyclerViewAdapter() {
        BasicRecyclerAdapter<T> tBasicRecyclerAdapter = setRecyclerAdapter();
        return tBasicRecyclerAdapter;
    }

    protected abstract BasicRecyclerAdapter<T> setRecyclerAdapter();

    @Override
    public void onItemClicked(View view, int position) {
        onRecyclerItemClicked(view, position);
    }

    @Override
    public void onItemLongClicked(View view, int position) {
        onRecyclerItemLongClicked(view, position);
    }

    protected abstract void onRecyclerItemClicked(View view, int position);

    protected abstract void onRecyclerItemLongClicked(View view, int position);

}
