/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Soo
 */
public abstract class ComplexViewHolderAdapter<T, VH extends ViewHolderAdapter.ViewHolder<T>> extends ViewHolderAdapter<T, VH> {
    
    private boolean emptyDisplay;
    
    public ComplexViewHolderAdapter(Context context) {
        this(context, false);
    }

    public ComplexViewHolderAdapter(Context context, boolean emptyDisplay) {
        super(context);
        this.emptyDisplay = emptyDisplay;
    }
    
    @Override
    public int getCount() {
        if (getEmptyDisplay()) {
            return 1;
        }
        return super.getCount();
    }
    
    @Override
    public Object getItem(int position) {
        if (getEmptyDisplay()) {
            return null;
        }
        return super.getItem(position);
    }
    
    public boolean getEmptyDisplay() {
        if (emptyDisplay && super.getCount() == 0) {
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getEmptyDisplay()) {
            return getEmptyView(position, parent);
        } else {
            if (convertView != null) {
                VH viewHolder = (VH) convertView.getTag();
                if (viewHolder == null) {
                    convertView = null;
                }
            }
            return super.getView(position, convertView, parent);
        }
    }
    
    protected abstract View getEmptyView(int position, ViewGroup parent);
}
