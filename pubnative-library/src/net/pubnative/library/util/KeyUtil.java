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

import static org.droidparts.util.Strings.getMD5;
import static org.droidparts.util.Strings.getSHA1;
import static org.droidparts.util.Strings.isEmpty;
import static org.droidparts.util.Strings.isNotEmpty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import net.pubnative.library.PubNativeContract.RequestInfo;
import net.pubnative.library.PubNativeContract.UserIdentifier;

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

	public static String getDefaultVal(Context ctx, String key) {
		String mac = IdUtil.getMacAddress(ctx);
		String imei = IdUtil.getIMEI(ctx);
		String advId = IdUtil.getAdvertisingId(ctx);
		Location loc = IdUtil.getLastLocation(ctx);
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
			return (loc != null) ? String.valueOf(loc.getLatitude()) : null;
		case RequestInfo.LONG:
			return (loc != null) ? String.valueOf(loc.getLongitude()) : null;
		case UserIdentifier.MAC_ADDRESS:
			return mac;
		case UserIdentifier.MAC_ADDRESS_SHA1:
			return sha1(mac);
		case UserIdentifier.MAC_ADDRESS_MD5:
			return md5(mac);
		case UserIdentifier.ANDROID_IMEI:
			return imei;
		case UserIdentifier.ANDROID_IMEI_SHA1:
			return sha1(imei);
		case UserIdentifier.ANDROID_IMEI_MD5:
			return md5(imei);
		case UserIdentifier.ANDROID_ADVERTISER_ID:
			return advId;
		case UserIdentifier.ANDROID_ADVERTISER_ID_SHA1:
			return sha1(advId);
		case UserIdentifier.ANDROID_ADVERTISER_ID_MD5:
			return md5(advId);
		case UserIdentifier.NO_USER_ID:
			return isEmpty(advId) ? "1" : "0";
		default:
			return null;
		}
	}

	private static int getSW(Context ctx) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		return (int) (Math.min(dm.widthPixels, dm.heightPixels) / dm.density);
	}

	private static String sha1(String str) {
		return isNotEmpty(str) ? getSHA1(str) : "";
	}

	private static String md5(String str) {
		return isNotEmpty(str) ? getMD5(str) : "";
	}

	private KeyUtil() {

	}

}
