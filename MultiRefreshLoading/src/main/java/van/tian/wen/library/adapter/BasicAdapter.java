package van.tian.wen.library.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected boolean refreshVisible; // 是否刷新可视区域

    public BasicAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<T> list, boolean clearPrevious) {
        if (mList != null && clearPrevious) {
            mList.clear();
        }
        mList = list;
    }

    public void setRefreshVisible(boolean refreshVisible) {
        this.refreshVisible = refreshVisible;
    }

    public boolean isRefreshVisible() {
        return refreshVisible;
    }

    public void addExtraList(List<T> list) {
        if (mList != null) {
            mList.addAll(list);
        } else {
            mList = list;
        }
    }

    public void preAddExtraList(List<T> list) {
        if (mList != null) {
            mList.addAll(0, list);
        } else {
            mList = list;
        }
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void destroyList() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
    }

    @Override
    public T getItem(int position) {
        if (mList == null || position >= mList.size() || position < 0) {
            return null;
        }
        return mList.get(position);
    }

    public void addItem(T t) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.add(t);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {

        if (mList != null && mList.size() > position) {
            mList.remove(position);
        }

    }

    public void clear() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
    }


}
