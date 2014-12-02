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

import static org.droidparts.util.ui.ViewUtils.setInvisible;

import java.util.WeakHashMap;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.api.PubNativeInterstitialsListener;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.logging.Event;
import net.pubnative.interstitials.logging.EventLogger;
import net.pubnative.interstitials.persist.InMem;
import net.pubnative.interstitials.util.ScreenUtil;
import net.pubnative.library.PubNative;
import net.pubnative.library.inner.PubNativeWorker;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.util.ui.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class AbstractDelegate implements OnClickListener {

	public static boolean backgroundRedirectEnabled = true;

	public static AbstractDelegate get(PubNativeInterstitialsActivity act,
			PubNativeInterstitialsType type, int adCount) {
		switch (type) {
		case INTERSTITIAL:
			return new InterstitialDelegate(act);
		case NATIVE:
			return new NativeDelegate(act, adCount);
		case LIST:
			return new ListDelegate(act, adCount);
		case CAROUSEL:
			return new CarouselDelegate(act, adCount);
		case VIDEO_BANNER:
			return new VideoBannerDelegate(act);
		case VIDEO_IN_FEED:
			return new VideoInFeedDelegate(act);
		default:
			throw new IllegalArgumentException(type.toString());
		}
	}

	protected final PubNativeInterstitialsActivity act;
	protected final int adCount;

	protected View contentView;
	protected View holderView;

	protected View closeBtn;

	public AbstractDelegate(PubNativeInterstitialsActivity act, int adCount) {
		this.act = act;
		this.adCount = adCount;
	}

	public void onCreate() {
		contentView = LayoutInflater.from(act).inflate(getContentLayoutId(),
				null);
		setInvisible(true, contentView);
		closeBtn = findViewById(R.id.btn_close);
		closeBtn.setOnClickListener(this);
		holderView = findViewById(R.id.view_holder);
		act.setContentView(contentView);
	}

	public final AdRequest getAdRequest(String appKey) {
		AdRequest req = new AdRequest(appKey, getAdFormat());
		req.fillInDefaults(act);
		req.setAdCount(adCount);
		return req;
	}

	protected AdFormat getAdFormat() {
		return AdFormat.NATIVE;
	}

	public abstract AdHolder<?>[] getAdHolders();

	public void onRotate() {
	}

	public void onLoadingDone() {
		setInvisible(false, contentView);
		notifyOnShown(getType());
	}

	@Override
	public void onClick(View v) {
		if (v == closeBtn) {
			act.finish();
		}
	}

	//

	public View getContentView() {
		return contentView;
	}

	public void onResume() {
		PubNativeWorker.onResume();
	}

	public void onPause() {
		PubNativeWorker.onPause();
	}

	public void onActivityFinish() {
		notifyOnClosed(getType());
	}

	//

	public abstract PubNativeInterstitialsType getType();

	protected abstract int getContentLayoutId();

	protected final <T extends View> T findViewById(int id) {
		return ViewUtils.findViewById(contentView, id);
	}

	//

	protected final void showInPlayStore(NativeAd ad) {
		if (backgroundRedirectEnabled) {
			PubNative.showInPlayStoreViaDialog(act, ad);
		} else {
			PubNative.showInPlayStoreViaBrowser(act, ad);
		}
		notifyOnTapped(getType(), ad);
	}

	//

	public static void init(Context ctx, String appKey) {
		EventLogger.logEvent(Event.INIT, "appKey: " + appKey);
		AbstractDelegate.ctx = ctx.getApplicationContext();
		InMem.appKey = appKey;
	}

	public static void addListener(PubNativeInterstitialsListener listener) {
		listeners.put(listener, null);
	}

	public static void removeListener(PubNativeInterstitialsListener listener) {
		listeners.remove(listener);
	}

	//

	public static void show(Activity act, PubNativeInterstitialsType type,
			int adCount) {
		EventLogger.logEvent(Event.SHOW, type, true);
		callingActivityIsFullScreen = ScreenUtil.isFullScreen(act);
		showCalled = true;
		Intent intent = PubNativeInterstitialsActivity.getShowPromosIntent(ctx,
				callingActivityIsFullScreen, type, adCount);
		ctx.startActivity(intent);
	}

	//

	private static boolean callingActivityIsFullScreen;

	private static boolean showCalled;

	//

	private static Context ctx;

	private static final WeakHashMap<PubNativeInterstitialsListener, Void> listeners = new WeakHashMap<PubNativeInterstitialsListener, Void>();

	static void onException(Exception e) {
		notifyOnError(e);
		if (showCalled) {
			showCalled = false;
			Intent intent = PubNativeInterstitialsActivity.getFinishIntent(ctx,
					callingActivityIsFullScreen);
			ctx.startActivity(intent);
		}
	}

	//

	private static void notifyOnClosed(PubNativeInterstitialsType type) {
		EventLogger.logEvent(Event.CLOSED, type);
		for (PubNativeInterstitialsListener l : listeners.keySet()) {
			l.onClosed(type);
		}
	}

	private static void notifyOnTapped(PubNativeInterstitialsType type,
			NativeAd ad) {
		EventLogger.logEvent(Event.TAPPED, ad);
		for (PubNativeInterstitialsListener l : listeners.keySet()) {
			l.onTapped(ad);
		}
	}

	private static void notifyOnShown(PubNativeInterstitialsType type) {
		EventLogger.logEvent(Event.SHOW, type, false);
		for (PubNativeInterstitialsListener l : listeners.keySet()) {
			l.onShown(type);
		}
	}

	public static void notifyOnError(Exception e) {
		EventLogger.logEvent(Event.ERROR, e);
		for (PubNativeInterstitialsListener l : listeners.keySet()) {
			l.onError(e);
		}
	}

}
