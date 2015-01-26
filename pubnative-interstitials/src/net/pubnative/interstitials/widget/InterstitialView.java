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
package net.pubnative.interstitials.widget;

import static org.droidparts.util.ui.ViewUtils.setGone;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.util.ScreenUtil;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InterstitialView extends LinearLayout {

	public InterstitialView(Context ctx) {
		super(ctx);
		init();
	}

	public InterstitialView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init();
	}

	public InterstitialView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.pn_view_interstitial, this);
		landscapeImageView = (ImageView) findViewById(R.id.view_game_image);
		portraitGameImageView = (ImageView) findViewById(R.id.view_game_image_portrait);
		setBackgroundColor(getResources().getColor(android.R.color.white));
		applyOrientation();
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		applyOrientation();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		applyOrientation();
	}

	private void applyOrientation() {
		boolean port = ScreenUtil.isPortrait(getContext());
		setOrientation(port ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
		setGone(port, portraitGameImageView);
		setGone(!port, landscapeImageView);
	}

	private ImageView landscapeImageView, portraitGameImageView;

}
