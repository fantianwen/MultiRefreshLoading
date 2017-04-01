package van.tian.wen.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public View getViewById(int resId) {
        if (resId == 0) {
            throw new IllegalArgumentException("resId maybe illegal");
        }
        return this.mView.findViewById(resId);
    }


    public <V> V getView(int resId) {
        if (resId == 0) {
            throw new IllegalArgumentException("resId maybe illegal");
        }
        return (V) this.mView.findViewById(resId);
    }

    public void setTextView(int textViewResId, CharSequence str) {
        ((TextView) getViewById(textViewResId)).setText(str);
    }

    public View getItemView() {
        return mView;
    }

    public void setTextForTextView(int textViewResId, String estateName) {
        setTextView(textViewResId, estateName);
    }
}
