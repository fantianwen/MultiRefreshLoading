package van.tian.wen.multirefreshloading;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("member/blogs")
    Call<Pagnation<MemberBlog>> getMemberBlogs(@Query("nameAlpha") String nameAlpha,
                                               @Query("pageSize") int pageSize);

}
