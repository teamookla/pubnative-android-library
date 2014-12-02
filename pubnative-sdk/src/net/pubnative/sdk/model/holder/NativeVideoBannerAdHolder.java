package net.pubnative.sdk.model.holder;

import net.pubnative.sdk.model.AdFormat;
import android.view.View;

public class NativeVideoBannerAdHolder extends NativeAdHolder {

	public int playButtonViewId;

	public NativeVideoBannerAdHolder(View view) {
		super(view);
	}

	@Override
	public AdFormat getFormat() {
		return AdFormat.NATIVE_VIDEO_BANNER;
	}

}
