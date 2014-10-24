package net.pubnative.interstitials.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
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

	public static boolean isTablet(Context ctx) {
		return is7Inch(ctx) || is10Inch(ctx);
	}

	public static boolean is7Inch(Context ctx) {
		return getSW(ctx) >= 600;
	}

	public static boolean is10Inch(Context ctx) {
		return getSW(ctx) >= 720;
	}

	private static int getSW(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return (int) (Math.min(dm.widthPixels, dm.heightPixels) / dm.density);
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
