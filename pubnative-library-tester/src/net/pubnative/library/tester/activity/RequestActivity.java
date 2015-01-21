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

import static org.droidparts.util.Strings.isNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.pubnative.library.PubNativeContract;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.tester.PrefsManager;
import net.pubnative.library.tester.R;
import net.pubnative.library.tester.contract.Contract;
import net.pubnative.library.util.KeyUtil;

import org.droidparts.activity.legacy.Activity;
import org.droidparts.adapter.holder.ViewHolder;
import org.droidparts.annotation.inject.InjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RequestActivity extends Activity implements OnClickListener,
		PubNativeContract {

	@InjectView(id = R.id.rb_response_native)
	private RadioButton nativeResponseRB;
	@InjectView(id = R.id.rb_response_image)
	private RadioButton imageResponseRB;

	@InjectView(id = R.id.view_table)
	private TableLayout tableView;

	@InjectView(id = R.id.btn_reset, click = true)
	private Button resetBtn;
	@InjectView(id = R.id.btn_raw_response, click = true)
	private Button rawResponseBtn;
	@InjectView(id = R.id.btn_rendered_response, click = true)
	private Button renderedResponseBtn;

	private PrefsManager prefsManager;

	@Override
	public void onPreInject() {
		setContentView(R.layout.activity_request);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getTitle() + " - " + getString(R.string.request));
		prefsManager = new PrefsManager(this);
		//
		addRows();
		fillInDefaults();
	}

	@Override
	protected void onPause() {
		super.onPause();
		prefsManager.setParams(getParams(true));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// XXX
		// setParams(prefsManager.getParams());
	}

	@Override
	public void onClick(View v) {
		if (v == resetBtn) {
			fillInDefaults();
		} else if (v == rawResponseBtn) {
			Intent intent = RawResponseActivity.getIntent(this, getAdRequest());
			startActivity(intent);
		} else if (v == renderedResponseBtn) {
			Intent intent = RenderedResponseActivity.getIntent(this,
					getAdRequest());
			startActivity(intent);
		}
	}

	private void addRows() {
		for (String key : KeyUtil.getAdRequestKeys()) {
			TableRow row = newRow(key);
			tableView.addView(row);
		}
	}

	private TableRow newRow(String key) {
		TableRow row = (TableRow) LayoutInflater.from(this).inflate(
				R.layout.view_request_table_row, null);
		Holder h = new Holder(row, key);
		row.setTag(h);
		return row;
	}

	private void fillInDefaults() {
		nativeResponseRB.setChecked(true);
		AdRequest req = new AdRequest(Contract.APP_TOKEN, AdFormat.NATIVE);
		req.fillInDefaults(this);
		req.setAdCount(4);
		req.setIconSize(100, 100);
		req.setBannerSize(480, 320);
		setParams(req.getParams());
	}

	private AdRequest getAdRequest() {
		AdFormat format = nativeResponseRB.isChecked() ? AdFormat.NATIVE
				: AdFormat.IMAGE;
		Map<String, String> params = getParams(false);
		AdRequest req = new AdRequest(params.get(RequestInfo.APP_TOKEN), format);
		req.getParams().putAll(params);
		return req;
	}

	private Map<String, String> getParams(boolean includeEmpty) {
		HashMap<String, String> map = new HashMap<>();
		for (Holder h : getRowHolders()) {
			String val = h.valView.getText().toString();
			if (includeEmpty || isNotEmpty(val)) {
				map.put(h.key, val);
			}
		}
		return map;
	}

	private void setParams(Map<String, String> params) {
		for (Holder row : getRowHolders()) {
			String val = params.get(row.key);
			if (val != null) {
				row.valView.setText(val);
			}
		}
	}

	private ArrayList<Holder> getRowHolders() {
		ArrayList<Holder> list = new ArrayList<>();
		for (int i = 0; i < tableView.getChildCount(); i++) {
			View row = tableView.getChildAt(i);
			Object tag = row.getTag();
			if (tag instanceof Holder) {
				list.add((Holder) tag);
			}
		}
		return list;
	}

	private static class Holder extends ViewHolder {

		final String key;

		@InjectView(id = R.id.view_key)
		private TextView keyView;

		@InjectView(id = R.id.view_val)
		EditText valView;

		public Holder(View view, String key) {
			super(view);
			this.key = key;
			keyView.setText(key);
		}
	}

}
