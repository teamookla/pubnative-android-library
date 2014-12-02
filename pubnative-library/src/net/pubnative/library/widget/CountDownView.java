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
package net.pubnative.library.widget;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import net.pubnative.library.R;

import org.droidparts.util.ui.ViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CountDownView extends FrameLayout {

	private ProgressBar progressBarView;
	private TextView progressTextView;

	public CountDownView(Context context) {
		super(context);
		init(context);
	}

	public CountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CountDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context ctx) {
		inflate(ctx, R.layout.pn_view_count_down, this);
		progressBarView = ViewUtils.findViewById(this, R.id.view_progress_bar);
		progressTextView = ViewUtils
				.findViewById(this, R.id.view_progress_text);
		RotateAnimation makeVertical = new RotateAnimation(0, -90,
				RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
		makeVertical.setFillAfter(true);
		progressBarView.startAnimation(makeVertical);
	}

	public void setProgress(int currentMs, int totalMs) {
		progressBarView.setMax(totalMs);
		progressBarView.setSecondaryProgress(totalMs);
		progressBarView.setProgress(currentMs);
		int remainSec = (totalMs - currentMs) / 1000 + 1;
		progressTextView.setText(String.valueOf(remainSec));
	}
}
