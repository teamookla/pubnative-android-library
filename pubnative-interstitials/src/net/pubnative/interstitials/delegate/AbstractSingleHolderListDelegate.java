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

import org.droidparts.adapter.widget.ArrayAdapter;
import org.droidparts.util.ui.ViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbstractSingleHolderListDelegate<T extends View> extends
		AbstractDelegate {

	private ListView listView;
	protected T view;

	public AbstractSingleHolderListDelegate(PubNativeInterstitialsActivity act,
			int adCount) {
		super(act, adCount);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listView = ViewUtils.findViewById(act, R.id.view_list);
		listView.setAdapter(new ListAdapter(act));
		view = makeView();
	}

	@Override
	protected final int getContentLayoutId() {
		return R.layout.pn_delegate_list;
	}

	protected int getRowCount() {
		return 8;
	}

	protected int getViewRow() {
		return 3;
	}

	protected abstract T makeView();

	private class ListAdapter extends ArrayAdapter<String> {

		public ListAdapter(Context ctx) {
			super(ctx);
		}

		@Override
		public int getCount() {
			return getRowCount();
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (position != getViewRow()) {
				view = LayoutInflater.from(getContext()).inflate(
						android.R.layout.simple_list_item_1, null);
				TextView tv = ViewUtils.findViewById(view, android.R.id.text1);
				tv.setText("Row " + position);
				return view;
			} else {
				return AbstractSingleHolderListDelegate.this.view;
			}
		}
	}

}
