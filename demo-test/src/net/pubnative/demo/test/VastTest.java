package net.pubnative.demo.test;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.pubnative.sdk.vast.VastAd;
import net.pubnative.sdk.vast.VastAd.Creative;
import net.pubnative.sdk.vast.VastAd.Creative.MediaFile;
import net.pubnative.sdk.vast.VastParser;

import org.droidparts.util.ResourceUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.test.AndroidTestCase;

public class VastTest extends AndroidTestCase {

	public void testVastParsing() throws Exception {
		String xml = readRawResource(R.raw.vast_3_example);
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		VastAd ad = new VastParser(getContext()).deserialize(doc
				.getFirstChild());
		assertEquals(8192328, ad.id);
		assertEquals("armani", ad.title);
		assertEquals("armani si preroll", ad.description);
		assertEquals(
				"http://bs.serving-sys.com/BurstingPipe/adServer.bs?cn=riis&pl=VAST&optOut=0&pos=2871&c=24&ai=16929219&pluid=0&ord=1434673444&dg=1777156&ta=-1&sessionid=8798449454214733861&pcp=",
				ad.impressionUrl);
		assertFalse(ad.creatives.isEmpty());
		Creative c = ad.creatives.get(0);
		assertEquals(16929219, c.adId);
		assertEquals("00:00:31", c.duration);
		assertEquals(10, c.trackingEvents.size());
		assertEquals(
				"http://bs.serving-sys.com/BurstingPipe/adServer.bs?cn=isi&pl=VAST&optOut=0&interactionsStr=16929219~~0^Click.Linear.55334985~0~1~1~0~0~55334985~0&pos=2871&ebRandom=1434673444&dg=1777156&ta=-1&sessionid=8798449454214733861&rtu=$$http://www.behindsi.com/?utm_source=Coolshark&utm_medium=Haberturk&utm_campaign=BehindSi$$",
				c.videoClickUrl);
		assertEquals(1, c.mediaFiles.size());
		MediaFile mf = c.mediaFiles.get(0);
		assertEquals(854, mf.width);
		assertEquals(
				"http://ds.serving-sys.com/BurstingRes/Site-13486/Type-16/0c31159e-bc34-4e74-b963-9efaee4f0226.mp4",
				mf.url);
	}

	private String readRawResource(int resId) {
		return ResourceUtils.readRawResource(getContext(), resId);
	}
}
