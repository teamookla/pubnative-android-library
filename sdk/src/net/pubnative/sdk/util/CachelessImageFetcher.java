package net.pubnative.sdk.util;

import org.droidparts.concurrent.thread.BackgroundThreadExecutor;
import org.droidparts.net.http.RESTClient;
import org.droidparts.net.image.ImageFetcher;

import android.content.Context;

public class CachelessImageFetcher extends ImageFetcher {

	public CachelessImageFetcher(Context ctx) {
		super(ctx, new BackgroundThreadExecutor(2, "ImageFetcher-Fetch"),
				new RESTClient(ctx), null, null);
	}
}