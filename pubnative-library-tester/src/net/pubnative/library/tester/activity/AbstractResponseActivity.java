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

import org.droidparts.activity.legacy.Activity;
import org.droidparts.annotation.inject.InjectBundleExtra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public abstract class AbstractResponseActivity extends Activity {

	private static final String AD_REQUEST = "ad_request";

	@InjectBundleExtra(key = AD_REQUEST)
	protected AdRequest req;

	protected static Intent getIntent(Context ctx, AdRequest req,
			Class<? extends AbstractResponseActivity> cls) {
		Intent intent = new Intent(ctx, cls);
		intent.putExtra(AD_REQUEST, req);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getTitle() + " - " + getString(R.string.response));
	}

	//

	private ProgressDialog loadingDialog;

	protected final void showLoading() {
		dismissLoading(null);
		loadingDialog = ProgressDialog.show(this, null,
				getString(R.string.loading___), true);
		loadingDialog.setCancelable(true);
		loadingDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
	}

	protected final void dismissLoading(Exception ex) {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			if (ex != null) {
				Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		loadingDialog = null;
	}

	//

}
