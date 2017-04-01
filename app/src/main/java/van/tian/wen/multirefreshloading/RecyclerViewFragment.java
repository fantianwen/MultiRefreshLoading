package van.tian.wen.multirefreshloading;

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
    protected BasicRecyclerAdapter<MemberBlog> setRecyclerAdapter() {
        if (mMemberBlogAdapter == null) {
            mMemberBlogAdapter = new MemberBlogAdapter(mContext);
        }
        return mMemberBlogAdapter;
    }
}
