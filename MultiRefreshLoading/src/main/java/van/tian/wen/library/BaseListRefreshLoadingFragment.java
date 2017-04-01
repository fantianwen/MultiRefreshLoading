package van.tian.wen.library;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import van.tian.wen.library.model.Pageable;

public abstract class BaseListRefreshLoadingFragment<Pagination extends Pageable, T> extends BaseAbstractRefreshLoadingFragment<Pagination, T> {

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


    @Override
    protected void onItemClicked(AdapterView<?> parent, View view, int position, long id) {
        onListItemClicked(parent, view, position, id);
    }

    @Override
    protected void onItemLongClicked(AdapterView<?> parent, View view, int position, long id) {
        onListItemLongClicked(parent, view, position, id);
    }

    protected abstract void onListItemClicked(AdapterView<?> parent, View view, int position, long id);

    protected abstract void onListItemLongClicked(AdapterView<?> parent, View view, int position, long id);

}
