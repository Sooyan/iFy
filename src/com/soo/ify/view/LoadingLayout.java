package com.soo.ify.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soo.ify.R;

public class LoadingLayout extends FrameLayout {
    
    public interface OnLoadListener {
        void onLoad(LoadingLayout loadingLayout);
    }
    
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADINGERROR = 2;
    public static final int STATE_LOADINGSUCCESS = 3;
    
    private FrameLayout loadingView;
    
    private LinearLayout loadingContainer;
    private LinearLayout loadingErrorContainer;
    
    private TextView loadingTxt;
    private TextView loadingETxt;
    
    private OnLoadListener onLoadListener;
    
    private boolean reLoadAble = true;
    private int currentState = STATE_LOADINGSUCCESS;
    
    public LoadingLayout(Context context) {
        super(context);
        init(null);
    }
    
    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    
    private void init(AttributeSet attrs) {
        loadingView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_loading, loadingView);
//        this.addView(loadingView);
        
        loadingContainer = (LinearLayout) loadingView.findViewById(R.id.view_loading_loadinglayout);
        loadingErrorContainer = (LinearLayout) loadingView.findViewById(R.id.view_loading_loadingerrorlayout);
        
        loadingTxt = (TextView) loadingContainer.findViewById(R.id.view_loading_loadinglayout_textview);
        loadingETxt = (TextView) loadingErrorContainer.findViewById(R.id.view_loading_loadingerrorlayout_textview);
        
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
            CharSequence loadingText = ta.getText(R.styleable.LoadingLayout_loadingText);
            int loadingTextColor = ta.getColor(R.styleable.LoadingLayout_loadingTextColor, Color.BLACK);
            boolean isShowLoadingText = ta.getBoolean(R.styleable.LoadingLayout_showLoadingText, true);
            
            CharSequence loadingErrorText = ta.getText(R.styleable.LoadingLayout_loadingErrorText);
            int loadingErrorColor = ta.getColor(R.styleable.LoadingLayout_loadingErrorTextColor, Color.BLACK);
            boolean isShowLoadingErrorText = ta.getBoolean(R.styleable.LoadingLayout_showLoadingErrorText, true);
            
            reLoadAble = ta.getBoolean(R.styleable.LoadingLayout_reLoadingEnable, true);
            
            if (TextUtils.isEmpty(loadingText)) {
                loadingText = "Loading...";
            }
            loadingTxt.setText(loadingText);
            
            loadingTxt.setTextColor(loadingTextColor);
            if (!isShowLoadingText) {
                loadingTxt.setVisibility(View.GONE);
            }
            
            if (TextUtils.isEmpty(loadingErrorText)) {
                loadingErrorText = "Please try again...";
            }
            loadingETxt.setText(loadingErrorText);
            loadingETxt.setTextColor(loadingErrorColor);
            if (!isShowLoadingErrorText) {
                loadingETxt.setVisibility(View.GONE);
            }
            
            int state = ta.getInt(R.styleable.LoadingLayout_firstState, STATE_LOADINGSUCCESS);
            show(state);
            
            ta.recycle();
        }
        
        loadingETxt.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (reLoadAble && onLoadListener != null) {
                    showLoading();
                }
            }
        });
    }
    
    public void showLoading() {
        if (currentState != STATE_LOADING) {
            show(STATE_LOADING);
            if (onLoadListener != null) {
                onLoadListener.onLoad(this);
            }
        }
    }
    
    public void showLoadingError() {
        if (currentState != STATE_LOADINGERROR) {
            show(STATE_LOADINGERROR);
        }
    }
    
    public void showLoadingSucces() {
        if (currentState != STATE_LOADINGSUCCESS) {
            show(STATE_LOADINGSUCCESS);
        }
    }
    
    private void show(int state) {
        switch (state) {
            case STATE_LOADING:
                if (this.getChildAt(1) != null) {
                    this.getChildAt(1).setVisibility(View.GONE);
                }
                loadingView.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.VISIBLE);
                loadingErrorContainer.setVisibility(View.GONE);
                break;
            case STATE_LOADINGSUCCESS:
                if (this.getChildAt(1) != null) {
                    this.getChildAt(1).setVisibility(View.VISIBLE);
                }
                loadingView.setVisibility(View.GONE);
                break;
            case STATE_LOADINGERROR:
                if (this.getChildAt(1) != null) {
                    this.getChildAt(1).setVisibility(View.GONE);
                }
                loadingView.setVisibility(View.VISIBLE);
                loadingContainer.setVisibility(View.GONE);
                loadingErrorContainer.setVisibility(View.VISIBLE);
                break;
                default:
                    if (this.getChildAt(1) != null) {
                        this.getChildAt(1).setVisibility(View.VISIBLE);
                    }
                    loadingView.setVisibility(View.GONE);
                    state = STATE_LOADINGSUCCESS;
                    break;
        }
        currentState = state;
    }
    
    @Override
    public void addView(View child) {
        if (getChildCount() > 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }

        super.addView(child, index, params);
    }
    
    public void setOnLoadListener(OnLoadListener listener) {
        this.onLoadListener = listener;
    }
    
    public int getCurrentState() {
        return currentState;
    }
}

