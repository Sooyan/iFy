package com.soo.ify.request;

public class Response<T> {
    
    private T data;
    
    private StringBuilder tracker;
    
    private RequestException exception;
    
    public Response() {
        this("unknow:");
    }
    
    public Response(String head) {
        this.tracker = new StringBuilder(head);
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public void appendTracker(String message) {
        if (message != null) {
            tracker.append("-->");
            tracker.append(message);
        }
    }
    
    public String getTracker() {
        return tracker.toString();
    }

}
