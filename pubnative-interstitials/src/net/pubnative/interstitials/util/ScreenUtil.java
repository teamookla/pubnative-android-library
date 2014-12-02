/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.interstitials.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {

	public static int getScreenDensityDpi(Context ctx) {
		return ctx.getResources().getDisplayMetrics().densityDpi;
	}

	public static int getScreenWidth(Context ctx) {
		return ctx.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context ctx) {
		return ctx.getResources().getDisplayMetrics().heightPixels;
	}

	@SuppressLint("NewApi")
	public static Point getRealScreenSize(Context ctx) {
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		Point p = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			d.getRealSize(p);
		} else {
			p.x = getScreenWidth(ctx);
			p.y = getScreenHeight(ctx);
		}
		return p;
	}

	//

	public static boolean isPortrait(Context ctx) {
		int orientation = ctx.getResources().getConfiguration().orientation;
		return (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public static boolean isFullScreen(Activity act) {
		int windowFlags = act.getWindow().getAttributes().flags;
		return (windowFlags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
	}

	private ScreenUtil() {
	}

}
