package com.soo.swallow.request;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.soo.swallow.request.Request.Callback;

public class AsyncCallback<Result> implements Callback<Result>, Handler.Callback{
    
    private static final int WHAT_START = 0xf01;
    private static final int WHAT_ONPROGRESS = 0xf02;
    private static final int WHAT_ONFINISH = 0xf03;
    
    private final Handler handler;
    private final Callback<Result> callback;
    
    public AsyncCallback(Looper looper, Callback<Result> callback) {
        this.handler = new Handler(looper, this);
        this.callback = callback;
    }
    
    @Override
    public void onStart(Request<Result> request, String message) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, null, message, 0, null);
        this.handler.obtainMessage(WHAT_START, ao).sendToTarget();
    }

    @Override
    public void onRequest(Request<Result> request, String message, int progress) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, null, message, progress, null);
        this.handler.obtainMessage(WHAT_ONPROGRESS, ao).sendToTarget();
    }

    @Override
    public void onFinish(Request<Result> request, Result result, String message, RequestException exception) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, result, message, 1, exception);
        this.handler.obtainMessage(WHAT_ONFINISH, ao).sendToTarget();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        if (msg == null || callback == null) {
            return false;
        }
        AsyncObject<Result> ao = (AsyncObject<Result>) msg.obj;
        switch (msg.what) {
            case WHAT_START:
                callback.onStart(ao.request, ao.message);
                break;
            case WHAT_ONPROGRESS:
                callback.onRequest(ao.request, ao.message, ao.progress);
                break;
            case WHAT_ONFINISH:
                callback.onFinish(ao.request, ao.result, ao.message, ao.exception);
                break;
        }
        return true;
    }
    
    static class AsyncObject<Result> {
        final Request<Result> request;
        final Result result;
        final String message;
        final int progress;
        final RequestException exception;
        AsyncObject(Request<Result> request, Result result, String message, int progress, RequestException exception) {
            this.request = request;
            this.result = result;
            this.message = message;
            this.progress = progress;
            this.exception = exception;
        }
    }

}
