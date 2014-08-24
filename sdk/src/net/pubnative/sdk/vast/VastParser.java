package net.pubnative.sdk.vast;

import org.droidparts.persist.serializer.XMLSerializer;

import android.content.Context;

public class VastParser extends XMLSerializer<VastAd> {

	public VastParser(Context ctx) {
		super(VastAd.class, ctx);
	}

}