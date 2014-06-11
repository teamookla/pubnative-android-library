package net.pubnative.sdk;

public interface PubNativeListener {

	void onError(Exception ex);

	void onLoaded();

}