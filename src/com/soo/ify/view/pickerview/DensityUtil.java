package com.soo.ify.view.pickerview;

import android.content.Context;

public class DensityUtil {
	private static float scale;

	/**
	 * ??��???????��?????辨�??�? dp ??????�? �????�? px(???�?)
	 */
	public static int dip2px(Context context, float dpValue) {
		if (scale == 0)
			scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ??��???????��?????辨�??�? px(???�?) ??????�? �????�? dp
	 */
	public static int px2dip(Context context, float pxValue) {
		if (scale == 0)
			scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
