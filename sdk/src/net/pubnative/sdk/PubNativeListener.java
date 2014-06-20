package net.pubnative.sdk;

public interface PubNativeListener {

	void onLoaded();

	void onError(Exception ex);

}