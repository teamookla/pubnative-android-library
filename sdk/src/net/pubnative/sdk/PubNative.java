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
package net.pubnative.sdk;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.pubnative.sdk.util.ViewUtil.getVisiblePercent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.pubnative.sdk.model.AdFormat;
import net.pubnative.sdk.model.holder.AdHolder;
import net.pubnative.sdk.model.holder.ImageAdHolder;
import net.pubnative.sdk.model.holder.NativeAdHolder;
import net.pubnative.sdk.model.request.AdRequest;
import net.pubnative.sdk.model.response.Ad;
import net.pubnative.sdk.task.GetAdsTask;
import net.pubnative.sdk.task.SendConfirmationTask;
import net.pubnative.sdk.util.CachelessImageFetcher;
import net.pubnative.sdk.util.WebRedirector;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.net.http.HTTPResponse;
import org.droidparts.net.image.ImageFetchListener;
import org.droidparts.net.image.ImageFetcher;
import org.droidparts.net.image.ImageReshaper;
import org.droidparts.util.L;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class PubNative {

	public static void showAd(String appToken, AdHolder<?>... holders) {
		AdRequest req = null;
		if (holders.length > 0) {
			AdFormat format = (holders[0].getClass() == NativeAdHolder.class) ? AdFormat.NATIVE
					: AdFormat.IMAGE;
			req = new AdRequest(appToken, format);
			req.setAdCount(holders.length);
		}
		showAd(req, holders);
	}

	public static void showAd(AdRequest req, AdHolder<?>... holders) {
		if (holders.length > 0) {
			Context ctx = holders[0].view.getContext();
			new GetAdsTask<>(ctx, req, makeResultListener(listener, holders))
					.execute();
		} else {
			notifyListenerOnError(new IllegalArgumentException("0 holders."));
		}
	}

	public static void setListener(PubNativeListener listener) {
		PubNative.listener = listener;
	}

	public static void setImageReshaper(ImageReshaper reshaper) {
		PubNative.reshaper = reshaper;
	}

	public static void showInPlayStoreViaBrowser(Activity act, Ad ad) {
		new WebRedirector(act, ad.getPackageName(), ad.clickUrl)
				.doBrowserRedirect();
	}

	public static void showInPlayStoreViaDialog(Activity act, Ad ad) {
		showInPlayStoreViaDialog(act, ad, 3000);
	}

	public static void showInPlayStoreViaDialog(Activity act, Ad ad, int timeout) {
		new WebRedirector(act, ad.getPackageName(), ad.clickUrl)
				.doBackgroundRedirect(timeout);
	}

	//

	@SuppressWarnings("unchecked")
	private static <T extends Ad> AsyncTaskResultListener<ArrayList<Ad>> makeResultListener(
			final PubNativeListener listener, final AdHolder<?>... holders) {
		AsyncTaskResultListener<ArrayList<Ad>> resultListener = new AsyncTaskResultListener<ArrayList<Ad>>() {

			@Override
			public void onAsyncTaskSuccess(ArrayList<Ad> result) {
				Context ctx = holders[0].view.getContext();
				setUp(ctx);
				if (result.isEmpty()) {
					notifyListenerOnLoaded();
				} else {
					counter = 0;
					for (int i = 0; i < result.size(); i++) {
						AdHolder<T> holder = (AdHolder<T>) holders[i];
						holder.ad = (T) result.get(i);
						processAd(holder);
					}
				}
			}

			@Override
			public void onAsyncTaskFailure(Exception ex) {
				notifyListenerOnError(ex);
			}

		};
		return resultListener;
	}

	private static void processAd(AdHolder<?> holder) {
		if (holder instanceof ImageAdHolder) {
			processImageAd((ImageAdHolder) holder);
		} else if (holder instanceof NativeAdHolder) {
			processNativeAd((NativeAdHolder) holder);
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static void processNativeAd(NativeAdHolder holder) {
		if (holder.titleView != null) {
			holder.titleView.setText(holder.ad.title);
		}
		if (holder.subtitleView != null) {
			holder.subtitleView.setText(holder.ad.category);
		}
		if (holder.ratingView != null) {
			holder.ratingView.setRating(holder.ad.storeRating);
		}
		if (holder.descriptionView != null) {
			holder.descriptionView.setText(holder.ad.description);
		}
		if (holder.downloadView != null) {
			holder.downloadView.setText(holder.ad.ctaText);
		}
		//
		if (holder.iconView != null) {
			imageFetcher.attachImage(holder.ad.iconUrl, holder.iconView,
					reshaper, 0, ifListener);
		}
		if (holder.bannerView != null) {
			imageFetcher.attachImage(holder.ad.bannerUrl, holder.bannerView,
					reshaper, 0, ifListener);
		}
		//
		scheduleConfirmation(holder.bannerView, holder.ad);
	}

	private static void processImageAd(ImageAdHolder holder) {
		if (holder.imageView != null) {
			imageFetcher.attachImage(holder.ad.imageUrl, holder.imageView,
					reshaper, 0, ifListener);
		}
		//
		scheduleConfirmation(holder.imageView, holder.ad);
	}

	//

	private static void scheduleConfirmation(View view, Ad ad) {
		boolean alreadyConfirmed = confirmedAdUrls.contains(ad
				.getConfirmationUrl());
		if (!alreadyConfirmed) {
			map.put(view, new Data(ad));
		}
	}

	//
	private static void notifyListenerOnLoaded() {
		if (listener != null) {
			listener.onLoaded();
		}
	}

	private static void notifyListenerOnError(Exception ex) {
		if (listener != null) {
			listener.onError(ex);
		}
	}

	//

	private static void setUp(Context ctx) {
		if (!started) {
			L.i("Start up.");
			imageFetcher = new CachelessImageFetcher(ctx);
			exec = Executors.newSingleThreadScheduledExecutor();
			exec.scheduleAtFixedRate(checkRunnable, CHECK_INTERVAL,
					CHECK_INTERVAL, MILLISECONDS);
			started = true;
		}
	}

	private static void tearDown() {
		if (started) {
			L.i("Shut down.");
			started = false;
			exec.shutdown();
			exec = null;
			imageFetcher = null;
			confirmedAdUrls.clear();
		}
	}

	private static volatile boolean started;

	private static ImageFetcher imageFetcher;
	private static ImageReshaper reshaper = null;
	private static PubNativeListener listener;

	private static ScheduledExecutorService exec;
	private static final WeakHashMap<View, Data> map = new WeakHashMap<>();

	private static int counter = 0;

	//

	private static final int CHECK_INTERVAL = 500;
	private static final int SHOWN_TIME = 1000;
	private static final int VISIBLE_PERCENT = 50;

	private static void check() {
		long now = System.currentTimeMillis();
		Iterator<View> it = map.keySet().iterator();
		while (it.hasNext()) {
			View v = it.next();
			Data d = map.get(v);
			boolean startedTracking = (d.firstAppeared > 0);
			if (startedTracking && (now - d.firstAppeared) > SHOWN_TIME) {
				confirmedAdUrls.add(d.ad.getConfirmationUrl());
				new SendConfirmationTask(v.getContext(), confirmationListener,
						d.ad).execute();
				it.remove();
			} else if (!startedTracking
					&& getVisiblePercent(v) > VISIBLE_PERCENT) {
				d.firstAppeared = now;
			}
		}
		if (map.isEmpty()) {
			tearDown();
		}

	}

	private static final HashSet<String> confirmedAdUrls = new HashSet<>();

	private static final Runnable checkRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				check();
			} catch (Exception e) {
				notifyListenerOnError(e);
			}
		}
	};

	private static final AsyncTaskResultListener<HTTPResponse> confirmationListener = new AsyncTaskResultListener<HTTPResponse>() {

		@Override
		public void onAsyncTaskSuccess(HTTPResponse resp) {
			L.d(resp);
		}

		@Override
		public void onAsyncTaskFailure(Exception ex) {
			L.w(ex);
		}
	};

	private static final ImageFetchListener ifListener = new ImageFetchListener() {

		@Override
		public void onFetchAdded(ImageView imageView, String imgUrl) {
			counter++;
		}

		@Override
		public void onFetchCompleted(ImageView imageView, String imgUrl,
				Bitmap bm) {
			countDown();
		}

		@Override
		public void onFetchFailed(ImageView imageView, String imgUrl,
				Exception e) {
			countDown();
			notifyListenerOnError(e);
		}

		@Override
		public void onFetchProgressChanged(ImageView imageView, String imgUrl,
				int kBTotal, int kBReceived) {
			// pass
		}

		private void countDown() {
			counter--;
			if (counter == 0) {
				notifyListenerOnLoaded();
			}
		}

	};

	private static class Data {

		final Ad ad;
		long firstAppeared = -1;

		Data(Ad ad) {
			this.ad = ad;
		}
	}

}
