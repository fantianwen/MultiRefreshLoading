package van.tian.wen.multirefreshloading;

import android.view.View;
import android.widget.AdapterView;

import van.tian.wen.library.BaseListRefreshLoadingFragment;
import van.tian.wen.library.adapter.BasicAdapter;

/**
 * Created by RadAsm on 17/4/1.
 */
public class ListFragment extends BaseListRefreshLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    @Override
    protected void onRequestData() {
        super.onRequestData();
    }

    @Override
    protected BasicAdapter<MemberBlog> setAdapter() {
        return super.setAdapter();
    }

    @Override
    protected void onListItemClicked(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onListItemLongClicked(AdapterView<?> parent, View view, int position, long id) {

    }

}
