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
import net.pubnative.library.model.holder.NativeAdHolder;
import android.view.View;

public class InterstitialDelegate extends AbstractDelegate {

	private NativeAdHolder[] holders;

	public InterstitialDelegate(PubNativeInterstitialsActivity act) {
		super(act, 1);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.INTERSTITIAL;
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.pn_delegate_interstitial;
	}

	@Override
	public NativeAdHolder[] getAdHolders() {
		return holders;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		holderView.setOnClickListener(this);
		createHolders();
	}

	private void createHolders() {
		NativeAdHolder holder = new NativeAdHolder(holderView);
		holder.iconViewId = R.id.view_icon;
		holder.bannerViewId = R.id.view_game_image;
		holder.portraitBannerViewId = R.id.view_game_image_portrait;
		holder.titleViewId = R.id.view_title;
		holder.ratingViewId = R.id.view_rating;
		holder.descriptionViewId = R.id.view_description;
		holder.downloadViewId = R.id.btn_download;
		holders = new NativeAdHolder[] { holder };
	}

	@Override
	public void onClick(View v) {
		if (v == holderView) {
			showInPlayStore(holders[0].ad);
		} else {
			super.onClick(v);
		}
	}

}
