package net.pubnative.interstitials;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static net.pubnative.interstitials.contract.PubNativeInterstitialsConstants.PUB_NATIVE_INTERSTITIALS;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.delegate.AbstractDelegate;
import net.pubnative.interstitials.logging.Event;
import net.pubnative.interstitials.logging.EventLogger;
import net.pubnative.interstitials.persist.InMem;
import net.pubnative.library.PubNative;
import net.pubnative.library.PubNativeListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public final class PubNativeInterstitialsActivity extends Activity implements
		PubNativeListener {

	public static Intent getShowPromosIntent(Context ctx, boolean fullScreen,
			PubNativeInterstitialsType type, int adCount) {
		return IntentData.getShowPromosIntent(ctx, fullScreen, type, adCount);
	}

	public static Intent getFinishIntent(Context ctx, boolean fullScreen) {
		return IntentData.getFinishIntent(ctx, fullScreen);
	}

	/* package */AbstractDelegate delegate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tryToWakeUp();
		requestWindowFeature(FEATURE_NO_TITLE);
		getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
		IntentData id = new IntentData(getIntent());
		if (id.isFullScreen()) {
			getWindow().addFlags(FLAG_FULLSCREEN);
		}
		PubNative.setListener(this);
		initFromIntentData(id, true);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		IntentData id = new IntentData(intent);
		if (id.isContentIntent()) {
			setIntent(intent);
		}
		initFromIntentData(id, true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		EventLogger.logEvent(Event.SCREEN_ROTATED);
		initFromIntentData(new IntentData(getIntent()), false);
	}

	//

	private void initFromIntentData(IntentData id, boolean onCreate) {
		if (id.isContentIntent()) {
			if (onCreate) {
				if (delegate == null || delegate.getType() != id.getType()) {
					delegate = AbstractDelegate.get(this, id.getType(),
							id.getAdCount());
				}
				delegate.onCreate();
				PubNative.showAd(delegate.getAdRequest(InMem.appKey),
						delegate.getNativeAdHolders());
			} else {
				delegate.onRotate();
			}
		} else if (id.isFinishIntent()) {
			finish();
		}
	}

	//

	@Override
	protected void onResume() {
		super.onResume();
		if (delegate != null) {
			delegate.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (delegate != null) {
			delegate.onPause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PubNative.onDestroy();
	}

	@Override
	protected void onUserLeaveHint() {
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		if (delegate != null) {
			delegate.onActivityFinish();
		}
		overridePendingTransition(0, 0);
	}

	@SuppressWarnings("deprecation")
	private void tryToWakeUp() {
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		int lightUpScreen = PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_BRIGHT_WAKE_LOCK;
		try {
			WakeLock wl = pm.newWakeLock(lightUpScreen,
					PUB_NATIVE_INTERSTITIALS);
			wl.acquire();
			wl.release();
		} catch (Exception e) {
			// Ok, android.permission.WAKE_LOCK" not requested.
		}
	}

	//

	static class IntentData {

		static Intent getShowPromosIntent(Context ctx, boolean fullScreen,
				PubNativeInterstitialsType type, int adCount) {
			Intent intent = getIntent(ctx, CONTENT_INTENT, fullScreen);
			intent.putExtra(EXTRA_TYPE, type);
			intent.putExtra(EXTRA_AD_COUNT, adCount);
			return intent;
		}

		static Intent getFinishIntent(Context ctx, boolean fullScreen) {
			Intent intent = getIntent(ctx, FINISH_INTENT, fullScreen);
			return intent;
		}

		private static Intent getIntent(Context ctx, String type,
				boolean fullScreen) {
			Intent intent = new Intent(ctx,
					PubNativeInterstitialsActivity.class);
			intent.setFlags(LAUNCH_FLAGS);
			intent.putExtra(INTENT_TYPE, type);
			intent.putExtra(EXTRA_FULL_SCREEN, fullScreen);
			return intent;
		}

		private static final int LAUNCH_FLAGS = FLAG_ACTIVITY_NEW_TASK
				| FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP
				| FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

		private static final String INTENT_TYPE = "intent_type";

		private static final String CONTENT_INTENT = "intent_content";
		private static final String FINISH_INTENT = "intent_finish";

		private static final String EXTRA_TYPE = "type";
		private static final String EXTRA_AD_COUNT = "ad_count";
		private static final String EXTRA_FULL_SCREEN = "full_screen";

		private static final String EXTRA_SHOW_LOADING = "show_loading";

		private final Intent intent;

		IntentData(Intent intent) {
			this.intent = intent;
		}

		boolean isFullScreen() {
			return intent.getBooleanExtra(EXTRA_FULL_SCREEN, false);
		}

		boolean isContentIntent() {
			return CONTENT_INTENT.equals(intent.getStringExtra(INTENT_TYPE));
		}

		boolean isFinishIntent() {
			return FINISH_INTENT.equals(intent.getStringExtra(INTENT_TYPE));
		}

		boolean isShowLoading() {
			return intent.getBooleanExtra(EXTRA_SHOW_LOADING, false);
		}

		PubNativeInterstitialsType getType() {
			return (PubNativeInterstitialsType) intent
					.getSerializableExtra(EXTRA_TYPE);
		}

		int getAdCount() {
			return intent.getIntExtra(EXTRA_AD_COUNT, -1);
		}

	}

	@Override
	public void onLoaded() {
		delegate.onLoadingDone();
	}

	@Override
	public void onError(Exception ex) {
		AbstractDelegate.notifyOnError(ex);
		finish();
	}

}
