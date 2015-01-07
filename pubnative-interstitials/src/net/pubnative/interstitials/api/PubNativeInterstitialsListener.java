package net.pubnative.interstitials.api;

import net.pubnative.library.model.response.NativeAd;

public interface PubNativeInterstitialsListener {

	void onShown(PubNativeInterstitialsType type);

	void onTapped(NativeAd ad);

	void onClosed(PubNativeInterstitialsType type);

	void onError(Exception ex);

}
