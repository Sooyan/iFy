package com.soo.ify.view.pickerview;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * �???��??�?宽�??�?�?�?
 */
public class ScreenInfo {
	 private Activity activity;
	 /** �?�?宽度�????�?�?*/
	 private int width;
	 /**�?�?�?�?�????�?�?*/
	 private int height;
	 /**�?�?�?�?�?0.75 / 1.0 / 1.5�?*/
	 private float density;
	 /**�?�?�?�?DPI�?120 / 160 / 240�?*/
	 private int densityDpi;
	 public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

	public void setDensityDpi(int densityDpi) {
		this.densityDpi = densityDpi;
	}

	public ScreenInfo(Activity activity){
		 this.activity = activity;
		 ini();
	 }
	 
	 private void ini(){
		 DisplayMetrics metric = new DisplayMetrics();
		 activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	     width = metric.widthPixels;  
	     height = metric.heightPixels; 
	     density = metric.density;  
	     densityDpi = metric.densityDpi;  
	 }
	 
	
}
