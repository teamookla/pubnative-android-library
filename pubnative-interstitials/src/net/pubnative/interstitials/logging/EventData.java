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
