package van.tian.wen.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicRecyclerAdapter<E> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context mContext;
    protected List<E> mLists;

    public boolean hasSelect = false;
    private View mHeaderView;
    private View mFootView;
    private boolean mFootViewClickable;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_HEADER = 1;
    public static final int VIEW_TYPE_FOOTER = 2;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BasicRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public BasicRecyclerAdapter(List<E> lists) {
        this.mLists = lists;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            if (mHeaderView == null) {
                throw new NullPointerException("the head view should not be null");
            } else {
                return new BaseRecyclerViewHolder(mHeaderView);
            }
        }

        if (viewType == VIEW_TYPE_NORMAL) {
            return new BaseRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(provideLayoutRes(), parent, false));
        }

        if (viewType == VIEW_TYPE_FOOTER) {
            if (mFootView == null) {
                throw new NullPointerException("the foot view should not be null");
            } else {
                return new BaseRecyclerViewHolder(mFootView);
            }
        }

        return null;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFootView == null) {
            return mLists.size();
        } else if (mHeaderView == null) {
            return mLists.size() + 1;
        } else if (mFootView == null) {
            return mLists.size() + 1;
        } else {
            return mLists.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null) {
            if (position == 0) {
                return VIEW_TYPE_HEADER;
            }
        }

        if (mFootView != null) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_FOOTER;
            }
        }

        return VIEW_TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        // fantianwen footView是否可以点击
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            return;
        }

        if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            return;
        }

        int realPosition = 0;
        boolean canClick;
        if (mHeaderView != null) {
            if (getItemCount() > 1 && position > 0) {
                realPosition = position - 1;
                canClick = true;
            } else {
                canClick = false;
            }
        } else {
            canClick = true;
            realPosition = position;
        }
        if (canClick && this.onItemClickListener != null) {
            final int finalRealPosition = realPosition;
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(v, finalRealPosition);
                    }
                }
            });

            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClicked(v, finalRealPosition);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

        }

        bindView(holder, position);
    }

    protected abstract void bindView(BaseRecyclerViewHolder holder, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected abstract int provideLayoutRes();

    public List<E> addExtra(List<E> extra) {
        if (mLists != null) {
            if (extra != null) {
                mLists.addAll(extra);
            } else {
                throw new NullPointerException("extras should not be null");
            }
            return mLists;
        }
        return null;
    }

    public void clear() {
        if (mLists != null) {
            mLists.clear();
        }
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return this.mFootView;
    }

    public void setFootView(View footView) {
        this.mFootView = footView;
        notifyItemInserted(getItemCount() - 1);
    }

    public View getFootView() {
        return mFootView;
    }

    public void setFootViewClickable(boolean clickable) {
        this.mFootViewClickable = clickable;
    }

    public void setList(ArrayList<E> list, boolean clearPrevious) {
        if (list != null && clearPrevious) {
            list.clear();
        }
        mLists = list;
    }

    public E getItem(int position) {
        if (mLists != null && mLists.size() > 0) {
            if (mHeaderView != null) {
                if (position > 0) {
                    return mLists.get(position - 1);
                }
            } else {
                return mLists.get(position);
            }
        }
        return null;
    }

    public int getFootViewCount() {
        return mFootView == null ? 0 : 1;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(View view, int position);
    }

}
