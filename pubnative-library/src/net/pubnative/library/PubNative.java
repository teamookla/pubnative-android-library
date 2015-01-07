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
package net.pubnative.library;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.pubnative.library.util.ViewUtil.getVisiblePercent;
import static org.droidparts.util.Strings.isEmpty;
import static org.droidparts.util.ui.ViewUtils.setInvisible;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.holder.ImageAdHolder;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.Ad;
import net.pubnative.library.task.GetAdsTask;
import net.pubnative.library.task.SendConfirmationTask;
import net.pubnative.library.util.ImageFetcher;
import net.pubnative.library.util.ViewUtil;
import net.pubnative.library.util.WebRedirector;
import net.pubnative.library.widget.VideoPopup;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.net.http.HTTPResponse;
import org.droidparts.net.image.ImageFetchListener;
import org.droidparts.net.image.ImageReshaper;
import org.droidparts.util.L;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PubNative {

	public static void showAd(String appToken, AdHolder<?>... holders) {
		AdRequest req = null;
		if (holders.length > 0) {
			ctx = holders[0].getView().getContext().getApplicationContext();
			req = new AdRequest(appToken, holders[0].getFormat());
			req.fillInDefaults(ctx);
			req.setAdCount(holders.length);
		}
		showAd(req, holders);
	}

	public static void showAd(AdRequest req, AdHolder<?>... holders) {
		counter = 0;
		loaded = false;
		if (holders.length > 0) {
			ctx = holders[0].getView().getContext().getApplicationContext();
			imageFetcher = new ImageFetcher(ctx);
			new GetAdsTask<>(ctx, req, makeResultListener(listener, holders))
					.execute();
		} else {
			onError(new IllegalArgumentException("0 holders."));
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

	public static void onPause() {
		if (loaded) {
			setExecRunning(false);
			for (Data d : holders) {
				if (d.isPlaying()) {
					d.mp.pause();
				}
			}
		}
	}

	public static void onResume() {
		if (loaded) {
			setExecRunning(true);
		}
	}

	public static void onDestroy() {
		holders.clear();
	}

	//

	@SuppressWarnings("unchecked")
	private static <T extends Ad> AsyncTaskResultListener<ArrayList<Ad>> makeResultListener(
			final PubNativeListener listener, final AdHolder<?>... holders) {
		AsyncTaskResultListener<ArrayList<Ad>> resultListener = new AsyncTaskResultListener<ArrayList<Ad>>() {

			@Override
			public void onAsyncTaskSuccess(ArrayList<Ad> result) {
				if (result.isEmpty()) {
					onLoaded();
				} else {
					for (int i = 0; i < result.size(); i++) {
						AdHolder<T> holder = (AdHolder<T>) holders[i];
						holder.ad = (T) result.get(i);
						processAd(holder);
					}
					for (int i = result.size(); i < holders.length; i++) {
						setInvisible(true, holders[i].getView());
					}
				}
			}

			@Override
			public void onAsyncTaskFailure(Exception ex) {
				onError(ex);
			}

		};
		return resultListener;
	}

	private static void processAd(AdHolder<?> holder) {
		Data d = new Data(holder);
		if (holder instanceof ImageAdHolder) {
			processImageAd(d);
		} else if (holder instanceof NativeAdHolder) {
			processNativeAd(d);
		} else {
			throw new IllegalArgumentException();
		}
		holders.add(d);
	}

	private static void processImageAd(Data d) {
		ImageAdHolder holder = (ImageAdHolder) d.holder;
		//
		ImageView iv = holder.getView(holder.imageViewId);
		if (iv != null) {
			imageFetcher.attachImage(holder.ad.imageUrl, iv, reshaper, 0,
					ifListener);
		}
	}

	private static void processNativeAd(Data d) {
		NativeAdHolder holder = (NativeAdHolder) d.holder;
		//
		TextView titleView = holder.getView(holder.titleViewId);
		if (titleView != null) {
			titleView.setText(holder.ad.title);
		}
		TextView subTitleView = holder.getView(holder.subTitleViewId);
		if (subTitleView != null) {
			subTitleView.setText(holder.ad.category);
		}
		RatingBar ratingView = holder.getView(holder.ratingViewId);
		if (ratingView != null) {
			ratingView.setRating(holder.ad.storeRating);
		}
		TextView descriptionView = holder.getView(holder.descriptionViewId);
		if (descriptionView != null) {
			descriptionView.setText(holder.ad.description);
			setInvisible(isEmpty(holder.ad.description), descriptionView);
		}
		TextView categoryView = holder.getView(holder.categoryViewId);
		if (categoryView != null) {
			categoryView.setText(holder.ad.category);
		}
		TextView downloadView = holder.getView(holder.downloadViewId);
		if (downloadView != null) {
			downloadView.setText(holder.ad.ctaText);
		}
		//
		ImageView iconView = holder.getView(holder.iconViewId);
		if (iconView != null) {
			imageFetcher.attachImage(holder.ad.iconUrl, iconView, reshaper, 0,
					ifListener);
		}
		ImageView bannerView = holder.getView(holder.bannerViewId);
		if (bannerView != null) {
			imageFetcher.attachImage(holder.ad.bannerUrl, bannerView, reshaper,
					0, ifListener);
			//
			// TextureView textureView = holder.getView(holder.textureViewId);
			// if (textureView != null) {
			// setInvisible(true, textureView);
			// if (holder.ad.videoUrl != null) {
			// d.mp = new MediaPlayer();
			// try {
			// d.mp.setDataSource(holder.ad.videoUrl);
			// initVideo(textureView, bannerView, d);
			// } catch (Exception e) {
			// d.mp = null;
			// onError(e);
			// }
			// }
			// }
		}
		ImageView portraitBannerView = holder
				.getView(holder.portraitBannerViewId);
		if (portraitBannerView != null) {
			imageFetcher.attachImage(holder.ad.portraitBannerUrl,
					portraitBannerView, reshaper, 0, ifListener);
		}
	}

	private static void initVideo(final TextureView tv, final ImageView iv,
			final Data d) {
		//
		ViewUtil.setSize(tv, 1, 1);
		//
		ViewUtil.setSurface(d.mp, tv);
		//
		d.mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				ViewUtil.setSize(tv, iv.getWidth(), iv.getHeight());
				//
				d.preparing = false;
				d.prepared = true;
				mp.start();
				setInvisible(true, iv);
				setInvisible(false, tv);
			}
		});
		d.mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				d.played = true;
				setInvisible(false, iv);
				// mp.seekTo(mp.getDuration());
			}
		});
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new VideoPopup(d.mp, tv).show();
			}
		});
	}

	//

	private static void onLoaded() {
		loaded = true;
		if (listener != null) {
			listener.onLoaded();
		}
		setExecRunning(true);

	}

	private static void onError(Exception ex) {
		if (listener != null) {
			listener.onError(ex);
		}
	}

	//

	private static void setExecRunning(boolean running) {
		if (running) {
			setExecRunning(false);
			exec = Executors.newSingleThreadScheduledExecutor();
			exec.scheduleAtFixedRate(checkRunnable, CHECK_INTERVAL,
					CHECK_INTERVAL, MILLISECONDS);
		} else {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
	}

	//

	private static Context ctx;
	private static ImageFetcher imageFetcher;
	private static ImageReshaper reshaper = null;
	private static PubNativeListener listener;

	private static ScheduledExecutorService exec;
	private static final HashSet<Data> holders = new HashSet<>();

	private static int counter;
	private static boolean loaded;

	//

	private static final int CHECK_INTERVAL = 500;
	private static final int SHOWN_TIME = 1000;
	private static final int VISIBLE_PERCENT = 50;

	private static void checkConfirmation() {
		long now = System.currentTimeMillis();
		for (Data d : holders) {
			if (!d.confirmed) {
				View v = d.holder.getView();
				if (v != null) {
					boolean startedTracking = (d.firstAppeared > 0);
					if (startedTracking && (now - d.firstAppeared) > SHOWN_TIME) {
						d.confirmed = true;
						new SendConfirmationTask(ctx, confirmationListener,
								d.holder.ad).execute();
					} else if (!startedTracking
							&& getVisiblePercent(v) > VISIBLE_PERCENT) {
						d.firstAppeared = now;
					}
				}
			}
		}
	}

	private static void checkVideo() {
		for (Data d : holders) {
			if (d.mp != null) {
				View v = d.holder.getView();
				if (v != null) {
					boolean visibleEnough = getVisiblePercent(v) > VISIBLE_PERCENT;
					if (!d.played) {
						if (visibleEnough) {
							if (!isAnyPlaying()) {
								if (!d.preparing && !d.prepared) {
									d.preparing = true;
									d.mp.prepareAsync();
								} else if (d.prepared && !d.mp.isPlaying()) {
									d.mp.start();
								}
							}
						} else if (d.mp.isPlaying()) {
							d.mp.pause();
						}
					}
				}
			}
		}
	}

	private static boolean isAnyPlaying() {
		for (Data d : holders) {
			if (d.preparing || d.isPlaying()) {
				return true;
			}
		}
		return false;
	}

	private static void cleanUp() {
		Iterator<Data> it = holders.iterator();
		while (it.hasNext()) {
			Data d = it.next();
			if (d.confirmed && (d.mp == null || d.played)) {
				it.remove();
			}
		}
	}

	private static final Runnable checkRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				checkConfirmation();
				checkVideo();
				cleanUp();
			} catch (Exception e) {
				onError(e);
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
		public void onFetchCompleted(final ImageView imageView, String imgUrl,
				Bitmap bm) {
			countDown();
		}

		@Override
		public void onFetchFailed(ImageView imageView, String imgUrl,
				Exception e) {
			countDown();
			onError(e);
		}

		@Override
		public void onFetchProgressChanged(ImageView imageView, String imgUrl,
				int kBTotal, int kBReceived) {
			// pass
		}

		private void countDown() {
			counter--;
			if (counter == 0) {
				onLoaded();
			}
		}

	};

	private static class Data {

		final AdHolder<?> holder;

		long firstAppeared = -1;
		boolean confirmed;
		//
		MediaPlayer mp;
		boolean preparing;
		boolean prepared;
		boolean played;

		Data(AdHolder<?> holder) {
			this.holder = holder;
		}

		boolean isPlaying() {
			return (mp != null && mp.isPlaying());
		}

		@Override
		public boolean equals(Object o) {
			return holder.equals(o);
		}

		@Override
		public int hashCode() {
			return holder.hashCode();
		}
	}

}
