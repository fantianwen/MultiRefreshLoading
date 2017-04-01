package van.tian.wen.multirefreshloading;

import android.content.Context;
import android.widget.ImageView;

import van.tian.wen.library.adapter.BaseRecyclerViewHolder;
import van.tian.wen.library.adapter.BasicRecyclerAdapter;

/**
 * Created by RadAsm on 17/4/1.
 */
public class MemberBlogAdapter extends BasicRecyclerAdapter<MemberBlog> {

    public MemberBlogAdapter(Context context) {
        super(context);
    }

    @Override
    protected void bindView(BaseRecyclerViewHolder holder, int position) {
        MemberBlog item = getItem(position);
        holder.setTextForTextView(R.id.blogTitle, item.getTitle());
        holder.setTextForTextView(R.id.summary, item.getSummary().trim());
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.item_member_blog;
    }
}
