package van.tian.wen.library;

import android.view.View;
import android.view.ViewGroup;

import van.tian.wen.library.model.Pageable;

public class BaseListRefreshLoadingFragment<Pagination extends Pageable, T> extends BaseAbstractRefreshLoadingFragment<Pagination, T> {

    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_base_list_impl;
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
}
