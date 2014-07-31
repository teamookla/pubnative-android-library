package net.pubnative.sdk.util;

import android.os.Handler;
import android.os.Looper;

public final class Watchdog {

	private final int timeoutMs;
	private final Runnable r;

	private final Handler handler;

	public Watchdog(int timeoutMs, Runnable r) {
		this.timeoutMs = timeoutMs;
		this.r = r;
		handler = new Handler(Looper.getMainLooper());
	}

	public void start() {
		stop();
		handler.postDelayed(r, timeoutMs);
	}

	public void stop() {
		handler.removeMessages(0);
	}

}
