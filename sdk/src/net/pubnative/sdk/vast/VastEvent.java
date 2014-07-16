package net.pubnative.sdk.vast;

public enum VastEvent {

	CREATE_VIEW("creativeView"), START("start"), FIRST_QUARTILE("firstQuartile"), MIDPOINT(
			"midpoint"), THIRD_QUARTILE("thirdQuartile"), COMPLETE("complete"), MUTE(
			"mute"), UNMUTE("unmute"), PAUSE("pause"), FULL_SCREEN("fullscreen");

	public final String key;

	private VastEvent(String key) {
		this.key = key;
	}

}
