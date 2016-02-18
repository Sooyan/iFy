package com.soo.ify.request;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.soo.ify.request.Request.Callback;

public class AsyncCallback<Result> implements Callback<Result>{
    
    private static final int WHAT_START = 0xf01;
    private static final int WHAT_ONPROGRESS = 0xf02;
    private static final int WHAT_ONFINISH = 0xf03;
    
    private static Handler handler = new InnerHandler();
    private final Callback<Result> callback;
    
    public AsyncCallback(Callback<Result> callback) {
        this.callback = callback;
    }
    
    @Override
    public void onStart(Request<Result> request, String message) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, callback, null, message, 0, null);
        handler.obtainMessage(WHAT_START, ao).sendToTarget();
    }

    @Override
    public void onRequest(Request<Result> request, String message, int progress) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, callback, null, message, progress, null);
        handler.obtainMessage(WHAT_ONPROGRESS, ao).sendToTarget();
    }

    @Override
    public void onFinish(Request<Result> request, Result result, String message, RequestException exception) {
        AsyncObject<Result> ao = new AsyncObject<Result>(request, callback, result, message, 1, exception);
        handler.obtainMessage(WHAT_ONFINISH, ao).sendToTarget();
    }

    static class InnerHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            @SuppressWarnings("rawtypes")
            AsyncObject ao = (AsyncObject) msg.obj;
            switch (msg.what) {
                case WHAT_START:
                    ao.callback.onStart(ao.request, ao.message);
                    break;
                case WHAT_ONPROGRESS:
                    ao.callback.onRequest(ao.request, ao.message, ao.progress);
                    break;
                case WHAT_ONFINISH:
                    ao.callback.onFinish(ao.request, ao.result, ao.message, ao.exception);
                    break;
            }
        }
    }
    
    static class AsyncObject<Result> {
        final Request<Result> request;
        final Callback<Result> callback;
        final Result result;
        final String message;
        final int progress;
        final RequestException exception;
        AsyncObject(Request<Result> request, Callback<Result> callback, Result result, String message, int progress, RequestException exception) {
            this.request = request;
            this.callback = callback;
            this.result = result;
            this.message = message;
            this.progress = progress;
            this.exception = exception;
        }
    }

}
