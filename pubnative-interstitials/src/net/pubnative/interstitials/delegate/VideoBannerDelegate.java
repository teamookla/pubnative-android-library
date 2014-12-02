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
import net.pubnative.interstitials.widget.InterstitialView;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.holder.VideoAdHolder;
import android.view.LayoutInflater;
import android.view.View;

public class VideoBannerDelegate extends AbstractSingleHolderListDelegate<View> {

	protected VideoAdHolder holder;
	private View interstitialView;

	public VideoBannerDelegate(PubNativeInterstitialsActivity act) {
		super(act, 1);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.VIDEO_BANNER;
	}

	@Override
	protected AdFormat getAdFormat() {
		return AdFormat.VIDEO;
	}

	@Override
	protected View makeView() {
		View view = LayoutInflater.from(act).inflate(
				R.layout.pn_view_video_banner, null);
		holder = new VideoAdHolder(getContentView());
		holder.bannerViewId = R.id.view_banner;
		holder.videoViewId = R.id.view_video;
		holder.playButtonViewId = R.id.view_play;
		//
		interstitialView = new InterstitialView(act);
		holder.backViewHolder = new NativeAdHolder(interstitialView);
		holder.backViewHolder.iconViewId = R.id.view_icon;
		holder.backViewHolder.bannerViewId = R.id.view_game_image;
		holder.backViewHolder.portraitBannerViewId = R.id.view_game_image_portrait;
		holder.backViewHolder.titleViewId = R.id.view_title;
		holder.backViewHolder.ratingViewId = R.id.view_rating;
		holder.backViewHolder.descriptionViewId = R.id.view_description;
		holder.backViewHolder.downloadViewId = R.id.btn_download;
		//
		view.findViewById(R.id.view_banner).setOnClickListener(this);
		interstitialView.setOnClickListener(this);
		//
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v == interstitialView || v.getId() == R.id.view_banner) {
			showInPlayStore(holder.ad);
		} else {
			super.onClick(v);
		}
	}

	@Override
	public VideoAdHolder[] getAdHolders() {
		return new VideoAdHolder[] { holder };
	}

}
