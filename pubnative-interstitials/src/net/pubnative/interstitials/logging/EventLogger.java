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