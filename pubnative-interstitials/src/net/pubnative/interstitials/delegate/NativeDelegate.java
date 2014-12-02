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
package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.delegate.adapter.NativeAdHolderAdapter;
import net.pubnative.library.model.holder.NativeAdHolder;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NativeDelegate extends AbstractDelegate implements
		OnItemClickListener {

	private ListView listView;

	private NativeAdHolder[] holders;
	private NativeAdHolderAdapter adapter;

	public NativeDelegate(PubNativeInterstitialsActivity act, int adCount) {
		super(act, adCount);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.NATIVE;
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.pn_delegate_list;
	}

	@Override
	public NativeAdHolder[] getAdHolders() {
		return holders;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listView = findViewById(R.id.view_list);
		adapter = createAdapter(act);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		createHolders();
	}

	private void createHolders() {
		holders = new NativeAdHolder[adCount];
		for (int i = 0; i < adCount; i++) {
			holders[i] = adapter.makeAndAddHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showInPlayStore(adapter.getItem(position).ad);
	}

	protected NativeAdHolderAdapter createAdapter(Context ctx) {
		return new NativeAdHolderAdapter(ctx);
	}

}
