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
package net.pubnative.library.inner;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static net.pubnative.library.util.ViewUtil.getVisiblePercent;
import static org.droidparts.util.Strings.isEmpty;
import static org.droidparts.util.ui.ViewUtils.setInvisible;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.pubnative.library.PubNative;
import net.pubnative.library.PubNativeListener;
import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.holder.ImageAdHolder;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.holder.VideoAdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.Ad;
import net.pubnative.library.model.response.NativeAd;
import net.pubnative.library.task.GetAdsTask;
import net.pubnative.library.task.SendConfirmationTask;
import net.pubnative.library.util.ImageFetcher;
import net.pubnative.library.util.ViewUtil;
import net.pubnative.library.vast.VastAd;
import net.pubnative.library.widget.VideoPopup;
import net.pubnative.library.widget.ViewPopup;

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

public class PubNativeWorker {

	private static final int CHECK_INTERVAL = 500;
	private static final int SHOWN_TIME = 1000;
	private static final int VISIBLE_PERCENT = 50;

	//

	private static Context ctx;
	private static ImageFetcher imageFetcher;
	private static ImageReshaper reshaper = null;
	private static PubNativeListener listener;

	private static ScheduledExecutorService exec;
	private static final HashSet<WorkerItem<?>> workerItems = new HashSet<>();

	private static int imageCounter;
	private static boolean loaded;

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
		imageCounter = 0;
		loaded = false;
		if (holders.length > 0) {
			ctx = holders[0].getView().getContext().getApplicationContext();
			imageFetcher = new ImageFetcher(ctx);
			new GetAdsTask<>(ctx, req, makeAdsResultListener(listener, holders))
					.execute();
		} else {
			onError(new IllegalArgumentException("0 holders."));
		}
	}

	public static void setListener(PubNativeListener listener) {
		PubNativeWorker.listener = listener;
	}

	public static void setImageReshaper(ImageReshaper reshaper) {
		PubNativeWorker.reshaper = reshaper;
	}

	public static void onPause() {
		if (loaded) {
			setExecRunning(false);
			for (WorkerItem<?> wi : workerItems) {
				if (wi.isPlaying()) {
					wi.mp.pause();
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
		workerItems.clear();
	}

	//

	@SuppressWarnings("unchecked")
	private static <T extends Ad> AsyncTaskResultListener<ArrayList<Ad>> makeAdsResultListener(
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void processAd(AdHolder<?> holder) {
		WorkerItem<?> wi = new WorkerItem(holder);
		if (holder instanceof ImageAdHolder) {
			processImageAd((ImageAdHolder) holder);
		} else if (holder instanceof NativeAdHolder) {
			processNativeAd((NativeAdHolder) holder);
		} else if (holder instanceof VideoAdHolder) {
			processVideoAd((WorkerItem<VideoAdHolder>) wi);
			VideoAdHolder h = (VideoAdHolder) holder;
			if (h.backViewHolder != null) {
				h.backViewHolder.ad = (NativeAd) holder.ad;
				processNativeAd(h.backViewHolder);
			}
		} else {
			throw new IllegalArgumentException();
		}
		workerItems.add(wi);
	}

	private static void processImageAd(ImageAdHolder holder) {
		ImageView iv = holder.getView(holder.imageViewId);
		if (iv != null) {
			imageFetcher.attachImage(holder.ad.imageUrl, iv, reshaper, 0,
					ifListener);
		}
	}

	private static void processNativeAd(NativeAdHolder holder) {
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
		//
		ImageView bannerView = holder.getView(holder.bannerViewId);
		if (bannerView != null) {
			imageFetcher.attachImage(holder.ad.bannerUrl, bannerView, reshaper,
					0, ifListener);
		}
		ImageView portraitBannerView = holder
				.getView(holder.portraitBannerViewId);
		if (portraitBannerView != null) {
			imageFetcher.attachImage(holder.ad.portraitBannerUrl,
					portraitBannerView, reshaper, 0, ifListener);
		}
	}

	private static void processVideoAd(WorkerItem<VideoAdHolder> wi) {
		VideoAdHolder vah = (VideoAdHolder) wi.holder;
		ImageView bannerView = vah.getView(vah.bannerViewId);
		if (bannerView != null) {
			imageFetcher.attachImage(vah.ad.bannerUrl, bannerView, reshaper, 0,
					ifListener);
		}
		View playButton = vah.getView(vah.playButtonViewId);
		if (playButton != null) {
			setInvisible(true, playButton);
		}
		//
		TextureView videoView = vah.getView(vah.videoViewId);
		if (videoView != null) {
			setInvisible(true, videoView);
			VastAd vastAd = vah.ad.getVastAd(ctx);
			if (vastAd != null) {
				wi.mp = new MediaPlayer();
				try {
					wi.mp.setDataSource(vastAd.getVideoUrl());
					initVideo(videoView, bannerView, playButton, wi);
				} catch (Exception e) {
					wi.mp = null;
					onError(e);
				}
			}
		}
	}

	private static void initVideo(final TextureView videoView,
			final ImageView bannerView, final View playButton,
			final WorkerItem<VideoAdHolder> wi) {
		//
		ViewUtil.setSize(videoView, 1, 1);
		//
		ViewUtil.setSurface(wi.mp, videoView);
		//
		if (playButton != null) {
			playButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setInvisible(true, playButton);
					wi.mp.start();
					showVideoPopup(videoView, wi, null);
				}
			});
		}
		//
		wi.mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				ViewUtil.setSize(videoView, bannerView.getWidth(),
						bannerView.getHeight());
				//
				wi.preparing = false;
				wi.prepared = true;
				boolean startPlaying = true;
				if (playButton != null) {
					if (playButton.getVisibility() != View.VISIBLE) {
						setInvisible(false, playButton);
						startPlaying = false;
					}
				}
				if (startPlaying) {
					mp.start();
					setInvisible(true, bannerView);
					setInvisible(false, videoView);
				}
			}
		});
		wi.mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				wi.played = true;
				setInvisible(false, bannerView);
				if (popupView != null) {
					new ViewPopup(popupView.getContext(), popupView)
							.show(parentView);
					parentView = popupView = null;
				}
			}
		});
		videoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showVideoPopup(videoView, wi, videoView);
			}
		});
	}

	private static void showVideoPopup(View parent,
			WorkerItem<VideoAdHolder> wi, TextureView parentTv) {
		VideoPopup wp = new VideoPopup(wi, parentTv, videoPopupListener);
		VideoAdHolder vah = (VideoAdHolder) wi.holder;
		if (vah.backViewHolder != null) {
			parentView = parent;
			popupView = vah.backViewHolder.getView();
		}
		wp.show(parent);
		wi.fullScreen = true;
	}

	private static View parentView, popupView;

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

	private static void checkConfirmation() {
		long now = System.currentTimeMillis();
		for (WorkerItem<?> wi : workerItems) {
			if (!wi.confirmed) {
				View v = wi.holder.getView();
				if (v != null) {
					boolean startedTracking = (wi.firstAppeared > 0);
					if (startedTracking
							&& (now - wi.firstAppeared) > SHOWN_TIME) {
						wi.confirmed = true;
						new SendConfirmationTask(ctx, confirmationListener,
								wi.holder.ad).execute();
					} else if (!startedTracking
							&& getVisiblePercent(v) > VISIBLE_PERCENT) {
						wi.firstAppeared = now;
					}
				}
			}
		}
	}

	private static void checkVideo() {
		for (WorkerItem<?> wi : workerItems) {
			if (wi.mp != null) {
				View v = wi.holder.getView();
				if (v != null) {
					boolean visibleEnough = getVisiblePercent(v) > VISIBLE_PERCENT;
					if (!wi.played) {
						if (visibleEnough) {
							if (!isAnyPlaying()) {
								if (!wi.preparing && !wi.prepared) {
									wi.preparing = true;
									wi.mp.prepareAsync();
								} else if (wi.prepared && !wi.mp.isPlaying()) {
									if (wi.inFeedVideo() || wi.fullScreen) {
										wi.mp.start();
									}
								}
							}
						} else if (wi.mp.isPlaying()) {
							wi.mp.pause();
						}
					}
				}
			}
		}
	}

	private static boolean isAnyPlaying() {
		for (WorkerItem<?> wi : workerItems) {
			if (wi.preparing || wi.isPlaying()) {
				return true;
			}
		}
		return false;
	}

	private static void cleanUp() {
		Iterator<WorkerItem<?>> it = workerItems.iterator();
		while (it.hasNext()) {
			WorkerItem<?> wi = it.next();
			if (wi.confirmed && (wi.mp == null || wi.played)) {
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
			imageCounter++;
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
			imageCounter--;
			if (imageCounter == 0) {
				onLoaded();
			}
		}

	};

	private static VideoPopup.Listener videoPopupListener = new VideoPopup.Listener() {

		@Override
		public void didVideoPopupSkip(VideoPopup vp, WorkerItem<?> wi) {
			didVideoPopupClose(vp, wi);
			wi.mp.stop();
			PubNative.showInPlayStoreViaDialog((Activity) wi.getContext(),
					wi.holder.ad);
		}

		@Override
		public void didVideoPopupClose(VideoPopup vp, WorkerItem<?> wi) {
			popupView = parentView = null;
			vp.dismiss();
		}

		@Override
		public void didVideoPopupDismiss(WorkerItem<?> wi, TextureView parentTv) {
			wi.fullScreen = false;
			if (parentTv != null) {
				ViewUtil.setSurface(wi.mp, parentTv);
			} else {
				wi.mp.stop();
			}
		}

	};

}
