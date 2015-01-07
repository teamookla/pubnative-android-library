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
package net.pubnative.library.tester.activity;

import net.pubnative.library.tester.R;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.task.GetAdsJSONTask;

import org.droidparts.annotation.inject.InjectView;
import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class RawResponseActivity extends AbstractResponseActivity {

	public static Intent getIntent(Context ctx, AdRequest req) {
		return getIntent(ctx, req, RawResponseActivity.class);
	}

	@InjectView(id = R.id.view_text)
	private TextView viewText;

	@Override
	public void onPreInject() {
		setContentView(R.layout.activity_raw_response);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		showLoading();
		new GetAdsJSONTask(this, req, resultListener).execute();
	}

	private final AsyncTaskResultListener<JSONObject> resultListener = new AsyncTaskResultListener<JSONObject>() {

		@Override
		public void onAsyncTaskSuccess(JSONObject resp) {
			String jsonStr = "";
			try {
				jsonStr = resp.toString(2);
			} catch (JSONException e) {
				// won't happen
			}
			jsonStr = jsonStr.replace("\\/", "/");
			viewText.setText(jsonStr);
			Linkify.addLinks(viewText, Linkify.WEB_URLS);
			//
			dismissLoading(null);
		}

		@Override
		public void onAsyncTaskFailure(Exception ex) {
			dismissLoading(ex);
			finish();
		}

	};

}
