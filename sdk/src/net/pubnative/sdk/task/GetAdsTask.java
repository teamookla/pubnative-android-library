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
package net.pubnative.sdk.task;

import java.util.ArrayList;

import net.pubnative.sdk.Contract.Response;
import net.pubnative.sdk.model.AdFormat;
import net.pubnative.sdk.model.request.AdRequest;
import net.pubnative.sdk.model.response.Ad;
import net.pubnative.sdk.model.response.ImageAd;
import net.pubnative.sdk.model.response.NativeAd;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.persist.json.JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class GetAdsTask<T extends Ad> extends SimpleAsyncTask<ArrayList<T>> {

	private final AdRequest adRequest;
	private final JSONSerializer<? extends Ad> serializer;

	public GetAdsTask(Context ctx, AdRequest adRequest,
			AsyncTaskResultListener<ArrayList<T>> resultListener) {
		super(ctx, resultListener);
		this.adRequest = adRequest;
		Class<? extends Ad> cls = (adRequest.getAdFormat() == AdFormat.NATIVE) ? NativeAd.class
				: ImageAd.class;
		serializer = new JSONSerializer<>(cls, ctx);
	}

	@Override
	protected ArrayList<T> onExecute() throws Exception {
		JSONObject obj = new GetAdsJSONTask(getContext(), adRequest, null)
				.onExecute();
		JSONArray arr = obj.getJSONArray(Response.ADS);
		ArrayList<? extends Ad> list = serializer.deserialize(arr);
		return (ArrayList<T>) list;
	}

}
