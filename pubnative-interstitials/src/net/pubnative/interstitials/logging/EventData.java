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

import java.util.ArrayList;

import org.droidparts.util.Strings;

public class EventData {

	private Event event;

	private Boolean start;
	private Object data;

	private Exception ex;

	public EventData(Event event, Boolean start, Object data) {
		this.event = event;
		this.data = data;
		this.start = start;
		if (data instanceof Exception) {
			this.ex = (Exception) data;
		} else {
			this.data = data;
		}
	}

	@Override
	public String toString() {
		ArrayList<Object> parts = new ArrayList<Object>();
		parts.add(event);
		if (start != null) {
			parts.add(start ? "START" : "END");
		}
		if (data != null) {
			parts.add(data);
		}
		if (ex != null) {
			String msg = ex.getMessage();
			Throwable cause = ex.getCause();
			if (msg != null) {
				parts.add(msg);
			}
			if (cause != null) {
				parts.add(cause);
			}
		}
		return Strings.join(parts, " ");
	}
}
