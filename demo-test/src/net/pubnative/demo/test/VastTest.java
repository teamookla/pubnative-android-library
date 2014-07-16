package net.pubnative.demo.test;

import net.pubnative.sdk.vast.VastAd;
import net.pubnative.sdk.vast.VastParser;

import org.droidparts.util.ResourceUtils;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class VastTest extends InstrumentationTestCase {

	public void testVastParsing() throws Exception {
		String xml = readRawResource(R.raw.vast_3_example);
		VastAd ad = VastParser.parse(xml);
		assertFalse(ad.creatives.isEmpty());

	}

	private String readRawResource(int resId) {
		Context ctx = getInstrumentation().getContext();
		return ResourceUtils.readRawResource(ctx, resId);
	}
}
