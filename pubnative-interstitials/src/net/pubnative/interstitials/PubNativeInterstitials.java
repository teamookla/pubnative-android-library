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
package net.pubnative.interstitials;

import static net.pubnative.interstitials.contract.PubNativeInterstitialsConstants.PUB_NATIVE_INTERSTITIALS;
import net.pubnative.interstitials.api.PubNativeInterstitialsListener;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.contract.PubNativeInterstitialsConstants;
import net.pubnative.interstitials.delegate.AbstractDelegate;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class PubNativeInterstitials {

	private static String lastAppKey;

	public static void init(Context ctx, String appKey) {
		checkThread();
		log(PubNativeInterstitialsConstants.VERSION);
		if (!appKey.equals(lastAppKey)) {
			AbstractDelegate.init(ctx, appKey);
			lastAppKey = appKey;
		} else {
			log("Same id, skipping init.");
		}
	}

	public static void addListener(PubNativeInterstitialsListener listener) {
		checkThread();
		AbstractDelegate.addListener(listener);
	}

	public static void removeListener(PubNativeInterstitialsListener listener) {
		checkThread();
		AbstractDelegate.removeListener(listener);
	}

	public static void setBackgroundRedirectEnabled(boolean en) {
		AbstractDelegate.backgroundRedirectEnabled = en;
	}

	//

	public static void show(Activity act, PubNativeInterstitialsType type,
			int adCount) {
		checkThread();
		checkInited();
		AbstractDelegate.show(act, type, adCount);
	}

	private static void checkThread() {
		boolean mainThread = Looper.getMainLooper().getThread() == Thread
				.currentThread();
		if (!mainThread) {
			throw new RuntimeException("Has to be called on the main thread.");
		}
	}

	private static void checkInited() {
		if (lastAppKey == null) {
			throw new IllegalStateException(
					"PubNativeInterstitials.init(...) has to be called first.");
		}
	}

	private static void log(String msg) {
		Log.i(PUB_NATIVE_INTERSTITIALS, msg);
	}

}
