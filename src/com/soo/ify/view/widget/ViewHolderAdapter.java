package com.soo.ify.view.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ViewHolderAdapter<T, VH extends ViewHolderAdapter.ViewHolder<T>> extends BaseAdapter {
    
    public static abstract class ViewHolder<T> {
       
       public Context context;
       public View itemView;
        
        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
        
        public abstract void onBindNewData(T data, int position);
    }
    
    private Context context;
    
    private List<T> lisT = new ArrayList<T>();
    
    private int testCount = 0;
    
    public ViewHolderAdapter(Context context) {
        this.context = context;
    }
    
    public void setTestCase(int count) {
        this.testCount = count;
    }
    
    public void add(T data) {
        add(data, false);
    }
    
    public void add(T data, boolean head) {
        if (data == null) {
            throw new NullPointerException("Can`t allowed to add null data");
        }
        if (head) {
            lisT.add(0, data);
        } else {
            lisT.add(data);
        }
        lisT = onDataChanged(lisT);
        notifyDataSetChanged();
    }
    
    public void add(Collection<T> datas) {
        add(datas, false);
    }
    
    public void add(Collection<T> datas, boolean head) {
        if (datas == null) {
            throw new NullPointerException("Can`t allowed to add null datas");
        }
        if (head) {
            lisT.addAll(0, datas);
        } else {
            lisT.addAll(datas);
        }
        lisT = onDataChanged(lisT);
        notifyDataSetChanged();
    }
    
    public void setNewData(Collection<T> datas) {
        lisT.clear();
        add(datas);
    }
    
    public void removeData(T data) {
        if (data == null) {
            throw new NullPointerException("Can`t allowed to remove null data");
        }
        lisT.remove(data);
        lisT = onDataChanged(lisT);
        notifyDataSetChanged();
    }
    
    public List<T> getData() {
        return lisT;
    }
    
    protected List<T> onDataChanged(List<T> data) {
        return data;
    }
    
    @Override
    public int getCount() {
        if (testCount > 0) {
            return testCount;
        }
        return lisT.size();
    }
    
    @Override
    public Object getItem(int position) {
        if (testCount > 0) {
            return null;
        }
        return lisT.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        VH viewHolder = null;
        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent, position);
            convertView = viewHolder.itemView;
            if (convertView == null) {
                throw new IllegalArgumentException("The itemView must not be null in the" 
                            + viewHolder.getClass().getSimpleName());
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }
        
        Object obj = getItem(position);
        T t = null;
        if (obj != null) {
            t = (T) obj;
        }
        viewHolder.context = this.context;
        viewHolder.onBindNewData(t, position);
        
        return convertView;
    }
    
    protected abstract VH onCreateViewHolder(ViewGroup parent, int position);
    
    public Context getContext() {
        return context;
    }
    
}
