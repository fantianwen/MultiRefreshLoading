package van.tian.wen.multirefreshloading;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import van.tian.wen.library.BaseRecyclerRefreshingLoadingFragment;
import van.tian.wen.library.adapter.BasicRecyclerAdapter;

/**
 * Created by RadAsm on 17/3/31.
 */
public class RecyclerViewFragment extends BaseRecyclerRefreshingLoadingFragment<Pagnation<MemberBlog>, MemberBlog> {

    private MemberBlogAdapter mMemberBlogAdapter;

    @Override
    protected void onRequestData() {
        super.onRequestData();

        Call<Pagnation<MemberBlog>> memberBlogs = RetrofitClient.service(API.class).getMemberBlogs("akimotomanatsu", 20);
        memberBlogs.enqueue(new Callback<Pagnation<MemberBlog>>() {
            @Override
            public void onResponse(Call<Pagnation<MemberBlog>> call, final Response<Pagnation<MemberBlog>> response) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ((MainActivity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onRequestResult(response.body());
                            }
                        });

                    }
                }).start();


            }

            @Override
            public void onFailure(Call<Pagnation<MemberBlog>> call, Throwable t) {
                onRequestResultFail();
            }
        });

    }

    @Override
    protected BasicRecyclerAdapter<MemberBlog> setRecyclerAdapter() {
        if (mMemberBlogAdapter == null) {
            mMemberBlogAdapter = new MemberBlogAdapter(mContext);
        }
        return mMemberBlogAdapter;
    }

    @Override
    protected void onRecyclerItemClicked(View view, int position) {
        Toast.makeText(mContext, "clicked\n" + mMemberBlogAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRecyclerItemLongClicked(View view, int position) {
        Toast.makeText(mContext, "long clicked\n" + mMemberBlogAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

}
