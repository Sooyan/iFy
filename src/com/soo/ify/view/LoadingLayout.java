package com.soo.ify.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soo.ify.R;
import com.soo.ify.util.ViewFinder;

public class LoadingLayout extends FrameLayout {
    
    public interface OnLoadListener {
        void onLoad(LoadingLayout loadingLayout);
    }
    
    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADINGERROR = 2;
    public static final int STATE_LOADINGSUCCESS = 3;
    
    private FrameLayout loadingView;
    private LinearLayout loadingContainer;
    private LinearLayout errorToastContainer;
    
    private TextView loadingTxt;
    private TextView errorToastTxt;
    
    private OnLoadListener onLoadListener;
    
    private CharSequence loadingStr;
    private CharSequence errorToastStr;
    private int loadingStrColor;
    private int errorToastStrColor;
    
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
        loadingView = ViewFinder.inflate(getContext(), R.layout.view_loading, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(loadingView, params);
        
        loadingContainer = ViewFinder.findViewById(this, R.id.view_loading_loadinglayout);
        errorToastContainer = ViewFinder.findViewById(this, R.id.view_loading_loadingerrorlayout);
        loadingTxt = ViewFinder.findViewById(this, R.id.view_loading_loadinglayout_textview);
        errorToastTxt = ViewFinder.findViewById(this, R.id.view_loading_loadingerrorlayout_textview);
        
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
            CharSequence loadingText = ta.getText(R.styleable.LoadingLayout_loadingText);
            int loadingTextColor = ta.getColor(R.styleable.LoadingLayout_loadingTextColor, Color.BLACK);
            
            CharSequence errorToastText = ta.getText(R.styleable.LoadingLayout_loadingErrorText);
            int errorToastColor = ta.getColor(R.styleable.LoadingLayout_loadingErrorTextColor, Color.BLACK);
            
            reLoadAble = ta.getBoolean(R.styleable.LoadingLayout_reLoadingEnable, true);
            
            if (TextUtils.isEmpty(loadingText)) {
                loadingText = "Loading...";
            }
            setLoadingText(loadingText);
            setLoadingTextColor(loadingTextColor);
            
            if (TextUtils.isEmpty(errorToastText)) {
                errorToastText = "Please try again...";
            }
            setErrorToastText(errorToastText);
            setErrorToastColor(errorToastColor);
            
            currentState = ta.getInt(R.styleable.LoadingLayout_firstState, STATE_LOADINGSUCCESS);
            
            ta.recycle();
        }
        
        errorToastTxt.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (reLoadAble) {
                    showLoading();
                }
            }
        });
    }
    
    public void setLoadingText(CharSequence loadingText) {
        this.loadingStr = loadingText;
        loadingTxt.setText(loadingStr);
    }
    
    public void setLoadingTextColor(int color) {
        this.loadingStrColor = color;
        loadingTxt.setTextColor(loadingStrColor);
    }
    
    public void setErrorToastText(CharSequence errorToastText) {
        this.errorToastStr = errorToastText;
        errorToastTxt.setText(errorToastStr);
    }
    
    public void setErrorToastColor(int color) {
        this.errorToastStrColor = color;
        errorToastTxt.setTextColor(errorToastStrColor);
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
                errorToastContainer.setVisibility(View.GONE);
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
                errorToastContainer.setVisibility(View.VISIBLE);
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        show(currentState);
    }
    
    @Override
    public void addView(View child) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("LoadingLayout can host only one external direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
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

