package van.tian.wen.multirefreshloading;

import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import van.tian.wen.library.BaseAbstractRefreshLoadingFragment;
import van.tian.wen.library.adapter.BasicAdapter;

/**
 * Created by RadAsm on 17/4/1.
 */
public class BaseImplFragment extends BaseAbstractRefreshLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    @Override
    protected int provideLayoutResId() {
        // provide a layout id,or you can provide 0,we will use a default one.
        return R.layout.fragment_base_recycler_impl;
    }

    @Override
    protected void onRequestData() {
        // call API to get data
        Call<Pagnation<MemberBlog>> memberBlogs = RetrofitClient.service(API.class).getMemberBlogs("akimotomanatsu", 20);
        memberBlogs.enqueue(new Callback<Pagnation<MemberBlog>>() {
            @Override
            public void onResponse(Call<Pagnation<MemberBlog>> call, Response<Pagnation<MemberBlog>> response) {
                onRequestResult(response.body());
            }

            @Override
            public void onFailure(Call<Pagnation<MemberBlog>> call, Throwable t) {
                onRequestResultFail();
            }
        });

    }

    @Override
    protected int provideListViewResId() {
        // you can specify the list used in the UI
        // if you return 0 by default, we will use a default list view implemented by ListView。
        return 0;
    }

    @Override
    protected View getAdapterView(int position, View convertView, ViewGroup parent) {
        // set how to customize the view by adapter

        //Note: if you want to use the Adapter Class,just implement method of {setAdapter} below
        return null;
    }

    @Override
    protected BasicAdapter<MemberBlog> setAdapter() {
        return super.setAdapter();
    }
}
