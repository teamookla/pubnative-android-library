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
package net.pubnative.library.util;

import static org.droidparts.util.Strings.isEmpty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import net.pubnative.library.PubNativeContract.RequestInfo;
import net.pubnative.library.PubNativeContract.UserIdentifier;
import net.pubnative.library.util.IdUtil.Callback;

import org.droidparts.util.L;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.DisplayMetrics;

public class KeyUtil {

	private static String[] keys;

	public static String[] getAdRequestKeys() {
		if (keys == null) {
			ArrayList<String> list = new ArrayList<>();
			for (Class<?> cl : new Class<?>[] { RequestInfo.class,
					UserIdentifier.class }) {
				for (Field f : cl.getFields()) {
					Object val;
					try {
						val = f.get(null);
						if (val instanceof String) {
							list.add((String) val);
						}
					} catch (Exception e) {
						L.w(e);
					}
				}
			}
			keys = list.toArray(new String[list.size()]);
		}
		return keys;
	}

	public static String putDefaultVal(Context ctx, String key) {
		switch (key) {
		case RequestInfo.BUNDLE_ID:
			return IdUtil.getPackageName(ctx);
		case RequestInfo.USER_AGENT:
			return IdUtil.getUserAgent(ctx);
		case RequestInfo.OS:
			return "android";
		case RequestInfo.OS_VERSION:
			return Build.VERSION.RELEASE;
		case RequestInfo.DEVICE_MODEL:
			return Build.MODEL;
		case RequestInfo.LOCALE:
			return Locale.getDefault().getLanguage();
		case RequestInfo.DEVICE_RESOLUTION:
			DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
			return dm.widthPixels + "x" + dm.heightPixels;
		case RequestInfo.DEVICE_TYPE:
			return getSW(ctx) < 600 ? "phone" : "tablet";
		case RequestInfo.LAT:
			Location lat = IdUtil.getLastLocation(ctx);
			return (lat != null) ? String.valueOf(lat.getLatitude()) : null;
		case RequestInfo.LONG:
			Location lon = IdUtil.getLastLocation(ctx);
			return (lon != null) ? String.valueOf(lon.getLongitude()) : null;
		case UserIdentifier.ANDROID_ADVERTISER_ID:
			return getAdvId(ctx);
		case UserIdentifier.NO_USER_ID:
			return isEmpty(getAdvId(ctx)) ? "1" : "0";
		default:
			return null;
		}
	}

	private static String getAdvId(Context ctx) {
		PrefsManager pm = new PrefsManager(ctx);
		String advId = pm.getAdvId();
		if (isEmpty(advId)) {
			KeyUtil.pm = pm;
			IdUtil.getAdvertisingId(ctx, cb);
		}
		return advId;
	}

	private static PrefsManager pm;
	private static Callback cb = new Callback() {

		@Override
		public void didGetAdvId(String advId) {
			pm.setAdvId(advId);
		}
	};

	//

	private static int getSW(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return (int) (Math.min(dm.widthPixels, dm.heightPixels) / dm.density);
	}

	private KeyUtil() {

	}

}
