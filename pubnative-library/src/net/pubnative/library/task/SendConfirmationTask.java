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
package net.pubnative.library.task;

import net.pubnative.library.model.response.Ad;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.net.http.HTTPResponse;
import org.droidparts.net.http.RESTClient2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class SendConfirmationTask extends SimpleAsyncTask<HTTPResponse> {

	private final RESTClient2 restClient;
	private final Ad ad;

	public SendConfirmationTask(Context ctx,
			AsyncTaskResultListener<HTTPResponse> resultListener, Ad ad) {
		super(ctx, resultListener);
		restClient = new RESTClient2(ctx);
		this.ad = ad;
	}

	@Override
	protected HTTPResponse onExecute() throws Exception {
		Uri.Builder bldr = Uri.parse(ad.getConfirmationUrl()).buildUpon();
		if (ad instanceof NativeAd) {
			String pkgName = ((NativeAd) ad).storeId;
			boolean installed = isPackageInstalled(pkgName);
			bldr.appendQueryParameter("installed", installed ? "1" : "0");
		}
		return restClient.get(bldr.build().toString());
	}

	private boolean isPackageInstalled(String pkgName) {
		PackageManager pm = getContext().getPackageManager();
		try {
			pm.getPackageInfo(pkgName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

}
