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
package net.pubnative.tester.tests;

import javax.xml.parsers.DocumentBuilderFactory;
import net.pubnative.library.vast.VastAd;
import net.pubnative.library.vast.VastAd.Creative;
import net.pubnative.library.vast.VastAd.Creative.MediaFile;
import net.pubnative.library.vast.VastParser;
import net.pubnative.tester.tests.R;
import org.w3c.dom.Node;
import android.test.AndroidTestCase;

public class VastTest extends AndroidTestCase {

	public void testVastParsing() throws Exception {
		VastAd ad = getMockAd();
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

	private VastAd getMockAd() throws Exception {
		Node node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(getContext().getResources().openRawResource(
						R.raw.vast_3_mock)).getFirstChild();
		return new VastParser(getContext()).deserialize(node);
	}
}
