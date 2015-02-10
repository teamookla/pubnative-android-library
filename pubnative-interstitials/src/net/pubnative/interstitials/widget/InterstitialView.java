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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InterstitialView extends LinearLayout {

	private ImageView landscapeImageView, portraitImageView;

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
		portraitImageView = (ImageView) findViewById(R.id.view_game_image_portrait);
		setBackgroundColor(getResources().getColor(android.R.color.white));
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		applyOrientation();
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		applyOrientation();
	}

	private void applyOrientation() {
		View container = findViewById(R.id.view_interstitial_1_container);
		container.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		View descriptionView = findViewById(R.id.view_description);
		setGone(false, descriptionView);
		boolean gotLandscapeImage = (landscapeImageView.getDrawable() != null);
		boolean gotPortraitImage = (portraitImageView.getDrawable() != null);
		// XXX OpenRTB hack
		gotPortraitImage = false;
		gotLandscapeImage = true;
		// XXX OpenRTB hack
		boolean port = ScreenUtil.isPortrait(getContext());
		boolean swap = false;
		if (port) {
			swap = (gotPortraitImage && !gotLandscapeImage);
		} else {
			swap = (gotLandscapeImage && !gotPortraitImage);
			if (swap) {
				setGone(true, descriptionView);
				container.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
		if (swap) {
			port = !port;
		}
		setOrientation(port ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
		setGone(port, portraitImageView);
		setGone(!port, landscapeImageView);
	}
}
