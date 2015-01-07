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
import net.pubnative.library.tester.adapter.AbstractAdHolderAdapter;
import net.pubnative.library.tester.adapter.ImageAdAdapter;
import net.pubnative.library.tester.adapter.NativeAdAdapter;
import net.pubnative.library.PubNative;
import net.pubnative.library.PubNativeContract;
import net.pubnative.library.PubNativeListener;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.Ad;

import org.droidparts.annotation.inject.InjectView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;

public class RenderedResponseActivity extends AbstractResponseActivity
		implements PubNativeContract, OnItemClickListener {

	public static Intent getIntent(Context ctx, AdRequest req) {
		return getIntent(ctx, req, RenderedResponseActivity.class);
	}

	@InjectView(id = R.id.rb_redirect_background)
	private RadioButton backgroundRedirectRB;
	@InjectView(id = R.id.rb_redirect_browser)
	private RadioButton browserRedirectRB;

	@InjectView(id = android.R.id.list)
	private ListView listView;

	private AbstractAdHolderAdapter<? extends AdHolder<?>> adapter;

	@Override
	public void onPreInject() {
		setContentView(R.layout.activity_rendered_response);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showLoading();
		if (req.getAdFormat() == AdFormat.NATIVE) {
			adapter = new NativeAdAdapter(this);
		} else {
			adapter = new ImageAdAdapter(this);
		}
		backgroundRedirectRB.setChecked(true);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		//
		int count = Integer.valueOf(req.getParam(RequestInfo.AD_COUNT));
		AdHolder<Ad>[] holders = new AdHolder[count];
		for (int i = 0; i < count; i++) {
			holders[i] = (AdHolder<Ad>) adapter.makeAndAddHolder();
		}
		PubNative.setListener(listener);
		PubNative.showAd(req, holders);
	}
 
	@Override
	protected void onPause() {
		super.onPause();
		PubNative.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		PubNative.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PubNative.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Ad ad = adapter.getItem(position).ad;
		boolean background = backgroundRedirectRB.isChecked();
		if (background) {
			PubNative.showInPlayStoreViaDialog(this, ad);
		} else {
			PubNative.showInPlayStoreViaBrowser(this, ad);
		}
	}

	private final PubNativeListener listener = new PubNativeListener() {

		@Override
		public void onLoaded() {
			dismissLoading(null);
		}

		@Override
		public void onError(Exception ex) {
			dismissLoading(ex);
		}
	};

}
