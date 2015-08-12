package com.soo.swallow.view.util;

public class LoadPaging {
    
    public enum Options {
        LOADFIRST,
        LOADNEXTPAGE,
        REFRESH
    }

    private static final int DEFAULT_PAGE_COUNT = 10;

    public interface Callback {
        void onLoading(LoadPaging loadPaging, int totalCount, int position, int pageCount, Options option, Object obj);
    }
    
    public static LoadPaging getInstance(Callback callback) {
        return getInstance(DEFAULT_PAGE_COUNT, callback);
    }
    
    public static LoadPaging getInstance(int pageCount, Callback calback) {
        LoadPaging instance = new LoadPaging(calback);
        instance.setPageCount(pageCount);
        return instance;
    }
    
    private Callback callback;
    
    private int totalCount = 0;
    private int pageCount = 0;
    private int index = 0;
    
    private int tempIndex = 0;

    @Deprecated
    public LoadPaging(Callback callback) {
        this.callback = callback;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (this.totalCount < 0) {
            this.totalCount = 0;
        }
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        if (this.pageCount < 0) {
            this.pageCount = 0;
        }
    }

    public void skipCount(int count) {
        int position = index;
        position += count;
        moveIndexTo(position);
    }

    public void skipPage(int pages) {
        int position = index;
        int pagesCount = pages * pageCount;
        position += pagesCount;
        moveIndexTo(position);
    }
    
    public void moveIndexTo(int position) {
        if (position < 0) {
            position = 0;
        }
        if (position >= totalCount) {
            position = totalCount - 1;
        }
        index = position;
    }

    public void loadFirst(Object obj) {
        loading(0, 0, pageCount, Options.LOADFIRST, obj);
    }

    public void loadNextPage(Object obj) {
        if (index >= totalCount && totalCount > 0) {
            return;
        }
        loading(totalCount, index, pageCount, Options.LOADNEXTPAGE, obj);
    }

    public void refresh(Object obj) {
        loading(0, 0, pageCount, Options.REFRESH, obj);
    }
    
    private void loading(int totalCount, int position, int count, Options options, Object obj) {
        tempIndex = position + count;
        if (callback != null) {
            callback.onLoading(this, totalCount, position, count, options, obj);
        }
    }
    
    public boolean isOutofBounds(int position) {
        if (totalCount == 0) {
            return false;
        }
        if (position < 0) {
            return true;
        } 
        if (position + pageCount > totalCount) {
            return true;
        }
        return false;
    }
    
    public void flush() {
        if (tempIndex > totalCount) {
            tempIndex = totalCount;
        }
        if (tempIndex < 0) {
            tempIndex = 0;
        }
        index = tempIndex;
    }
}
