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

import java.lang.reflect.Method;

import org.droidparts.util.L;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.webkit.WebView;

public class IdUtil {

	public static String getIMEI(Context ctx) {
		String imei = null;
		try {
			TelephonyManager tm = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
		} catch (Exception e) {
			L.w(e);
		}
		return (imei != null) ? imei : "";
	}

	public static String getMacAddress(Context ctx) {
		String mac = null;
		try {
			WifiManager wifiMan = (WifiManager) ctx
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			mac = wifiInf.getMacAddress();
		} catch (Exception e) {
			L.w(e);
		}
		return (mac != null) ? mac : "";
	}

	public static String getPackageName(Context ctx) {
		PackageInfo pInfo = getPackageInfo(ctx);
		return (pInfo != null) ? pInfo.packageName : "";
	}

	public static String getVersionName(Context ctx) {
		PackageInfo pInfo = getPackageInfo(ctx);
		return (pInfo != null) ? pInfo.versionName : "";
	}

	private static PackageInfo getPackageInfo(Context ctx) {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			L.w("Error getting package info.");
			return null;
		}
	}

	public static String getAdvertisingId(final Context ctx) {
		if (adInfo == null) {
			new Thread() {
				@Override
				public void run() {
					try {
						Class<?> cls = Class
								.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
						Method m = cls.getMethod("getAdvertisingIdInfo",
								Context.class);
						adInfo = m.invoke(null, ctx);
					} catch (Exception e) {
						L.v(e);
					}
				}
			}.start();
		} else {
			try {
				Method m = adInfo.getClass().getMethod(
						"isLimitAdTrackingEnabled");
				boolean trackingLimited = (Boolean) m.invoke(adInfo);
				if (trackingLimited) {
					throw new IllegalStateException(
							"Using ad tracking id prohibited by user.");
				} else {
					m = adInfo.getClass().getMethod("getId");
					return (String) m.invoke(adInfo);
				}
			} catch (Exception e) {
				L.v(e);
			}

		}
		return "";
	}

	private static Object adInfo;

	public static Location getLastLocation(Context ctx) {
		LocationManager lm = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		Location loc = null;
		for (String prov : lm.getProviders(true)) {
			loc = lm.getLastKnownLocation(prov);
			if (loc != null) {
				break;
			}
		}
		return loc;
	}

	public static String getUserAgent(Context ctx) {
		return new WebView(ctx).getSettings().getUserAgentString();
	}

	private IdUtil() {
	}

}
