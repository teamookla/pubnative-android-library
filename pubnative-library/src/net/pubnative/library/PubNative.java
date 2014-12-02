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
package net.pubnative.library;

import net.pubnative.library.inner.PubNativeWorker;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.Ad;
import net.pubnative.library.util.WebRedirector;

import org.droidparts.net.image.ImageReshaper;

import android.app.Activity;

public class PubNative {

	public static void showAd(String appToken, AdHolder<?>... holders) {
		PubNativeWorker.showAd(appToken, holders);
	}

	public static void showAd(AdRequest req, AdHolder<?>... holders) {
		PubNativeWorker.showAd(req, holders);
	}

	public static void setListener(PubNativeListener listener) {
		PubNativeWorker.setListener(listener);
	}

	public static void setImageReshaper(ImageReshaper reshaper) {
		PubNativeWorker.setImageReshaper(reshaper);
	}

	public static void showInPlayStoreViaBrowser(Activity act, Ad ad) {
		new WebRedirector(act, ad.getPackageName(), ad.clickUrl)
				.doBrowserRedirect();
	}

	public static void showInPlayStoreViaDialog(Activity act, Ad ad) {
		showInPlayStoreViaDialog(act, ad, 3000);
	}

	public static void showInPlayStoreViaDialog(Activity act, Ad ad, int timeout) {
		new WebRedirector(act, ad.getPackageName(), ad.clickUrl)
				.doBackgroundRedirect(timeout);
	}

	public static void onPause() {
		PubNativeWorker.onPause();
	}

	public static void onResume() {
		PubNativeWorker.onResume();
	}

	public static void onDestroy() {
		PubNativeWorker.onDestroy();
	}

}
