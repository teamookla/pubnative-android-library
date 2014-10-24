package net.pubnative.interstitials.logging;

import android.os.Handler;
import android.os.Looper;

public class EventLogger {

	public static interface Listener {

		void onEvent(EventData data);

	}

	private static Handler handler;
	private static Listener listener;

	public static void setListener(Listener listener) {
		EventLogger.listener = listener;
	}

	public static void logEvent(Event event) {
		logEvent(event, null, null);
	}

	public static void logEvent(Event event, boolean start) {
		logEvent(event, null, start);
	}

	public static void logEvent(Event event, Object data) {
		logEvent(event, data, null);
	}

	public static void logEvent(final Event event, final Object data,
			final Boolean start) {
		if (listener != null && runningFromTesterApp()) {
			if (handler == null) {
				handler = new Handler(Looper.getMainLooper());
			}
			handler.post(new Runnable() {

				@Override
				public void run() {
					listener.onEvent(new EventData(event, start, data));
				}
			});
		}
	}

	private static boolean runningFromTesterApp() {
		if (testApp == null) {
			try {
				Class.forName("net.pubnative.interstitials.tester.MainActivity");
				testApp = true;
			} catch (ClassNotFoundException e) {
				testApp = false;
			}
		}
		return testApp;
	}

	private static Boolean testApp;

}