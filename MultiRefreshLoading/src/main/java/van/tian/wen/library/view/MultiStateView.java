package van.tian.wen.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import van.tian.wen.library.R;

/**
 * ViewGroup having four type of state
 */
public class MultiStateView extends FrameLayout {
    /**
     * loading state
     */
    public static final int VIEW_STATE_LOADING = 0;

    /**
     * error state
     */
    public static final int VIEW_STATE_ERROR = 1;

    /**
     * success state
     */
    public static final int VIEW_STATE_SUCCESS = 2;

    /**
     * unknown state
     */
    public static final int VIEW_STATE_UNKNOWN = 3;

    private View loadingView;
    private View errorView;
    private View successView;
    private View unknownView;

    private int viewState;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.multiStateView);

        int loadingViewResId = typedArray.getResourceId(R.styleable.multiStateView_loadingView, -1);
        if (loadingViewResId > -1) {
            loadingView = layoutInflater.inflate(loadingViewResId, this, false);
            addView(loadingView, loadingView.getLayoutParams());
        }

        int unknownViewResId = typedArray.getResourceId(R.styleable.multiStateView_unknownView, -1);
        if (unknownViewResId > -1) {
            unknownView = layoutInflater.inflate(unknownViewResId, this, false);
            addView(unknownView, unknownView.getLayoutParams());
        }

        int errorViewResId = typedArray.getResourceId(R.styleable.multiStateView_errorView, -1);
        if (errorViewResId > -1) {
            errorView = layoutInflater.inflate(errorViewResId, this, false);
            addView(errorView, errorView.getLayoutParams());
        }

        int successViewResId = typedArray.getResourceId(R.styleable.multiStateView_successView, -1);
        if (successViewResId > -1) {
            successView = layoutInflater.inflate(successViewResId, this, false);
            addView(successView, successView.getLayoutParams());
        }

        viewState = typedArray.getInt(R.styleable.multiStateView_viewState, VIEW_STATE_SUCCESS);

        switch (viewState) {
            case VIEW_STATE_LOADING:
                viewState = VIEW_STATE_LOADING;
                break;
            case VIEW_STATE_ERROR:
                viewState = VIEW_STATE_ERROR;
                break;
            case VIEW_STATE_SUCCESS:
                viewState = VIEW_STATE_SUCCESS;
                break;
            case VIEW_STATE_UNKNOWN:
            default:
                viewState = VIEW_STATE_UNKNOWN;
                break;
        }
        typedArray.recycle();
    }

    public MultiStateView setLoadingView(View _loadingView) {
        this.loadingView = _loadingView;
        addView(loadingView, loadingView.getLayoutParams());
        return this;
    }


    public MultiStateView setErrorView(View _errorView) {
        this.errorView = _errorView;
        addView(errorView, errorView.getLayoutParams());
        return this;
    }

    public MultiStateView setSuccessView(View _successView) {
        this.successView = _successView;
        addView(successView, successView.getLayoutParams());
        return this;
    }

    public MultiStateView setUnknownView(View _unknownView) {
        this.unknownView = _unknownView;
        addView(unknownView, unknownView.getLayoutParams());
        return this;
    }


    public View getSuccessView() {
        return this.successView;
    }

    public View getLoadingView() {
        return this.loadingView;
    }

    public View getErrorView() {
        return this.errorView;
    }

    public View getUnknownView() {
        return this.unknownView;
    }

    public int getViewState() {
        return this.viewState;
    }

    /**
     * set view's state
     *
     * @param viewState
     */
    public void setViewState(int viewState) {
        this.viewState = viewState;
    }

    /**
     * set view's state then refresh this view
     *
     * @param viewState
     */
    public void setViewStateAndValidate(int viewState) {
        this.viewState = viewState;
        setView();
    }

    /**
     * this method will be used after method of 'onResume'
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (successView == null) {
            throw new IllegalArgumentException("SuccessView is not defined!");
        }
        setView();
    }

    public void setView() {
        switch (viewState) {
            case VIEW_STATE_ERROR:
                if (errorView == null) {
                    throw new NullPointerException("Error View");
                }
                errorView.setVisibility(View.VISIBLE);
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_LOADING:
                if (loadingView == null) {
                    throw new NullPointerException("Loading View");
                }
                loadingView.setVisibility(View.VISIBLE);
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_SUCCESS:
                if (successView == null) {
                    throw new NullPointerException("Success View");
                }
                successView.setVisibility(View.VISIBLE);
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                }
                if (unknownView != null) {
                    unknownView.setVisibility(View.GONE);
                }
                break;
            case VIEW_STATE_UNKNOWN:
            default:
                if (unknownView == null) {
                    throw new NullPointerException("unknown View");
                }
                unknownView.setVisibility(View.VISIBLE);
                if (errorView != null) {
                    errorView.setVisibility(View.GONE);
                }
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);

                }
                if (successView != null) {
                    successView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setMultiViewState(int state) {
        this.viewState = state;
        setView();
    }

}
