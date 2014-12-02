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
package net.pubnative.library.model.response;

import net.pubnative.library.PubNativeContract;
import net.pubnative.library.vast.VastAd;

import org.droidparts.annotation.serialize.JSON;
import org.droidparts.model.Model;
import org.droidparts.persist.serializer.XMLSerializer;
import org.droidparts.util.L;
import org.w3c.dom.Node;

import android.content.Context;

public class NativeAd extends Ad implements
		PubNativeContract.Response.NativeFormat {
	private static final long serialVersionUID = 1L;

	@JSON(key = TITLE)
	public String title;

	@JSON(key = DESCRIPTION)
	public String description;

	@JSON(key = ICON_URL)
	public String iconUrl;

	@JSON(key = BANNER_URL)
	public String bannerUrl;
	@JSON(key = PORTRAIT_BANNER_URL)
	public String portraitBannerUrl;

	@JSON(key = CTA_TEXT)
	public String ctaText;

	@JSON(key = APP_DETAILS + JSON.SUB + STORE_ID)
	public String storeId;
	@JSON(key = APP_DETAILS + JSON.SUB + NAME, optional = true)
	public String name;
	@JSON(key = APP_DETAILS + JSON.SUB + PLATFORM, optional = true)
	public String platform;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW, optional = true)
	public String review;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW_PROS, optional = true)
	public String[] reviewPros;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW_CONS, optional = true)
	public String[] reviewCons;
	@JSON(key = APP_DETAILS + JSON.SUB + PUBLISHER, optional = true)
	public String publisher;
	@JSON(key = APP_DETAILS + JSON.SUB + DEVELOPER, optional = true)
	public String developer;
	@JSON(key = APP_DETAILS + JSON.SUB + VERSION, optional = true)
	public String version;
	@JSON(key = APP_DETAILS + JSON.SUB + SIZE, optional = true)
	public String size;
	@JSON(key = APP_DETAILS + JSON.SUB + AGE_RATING, optional = true)
	public String ageRating;
	@JSON(key = APP_DETAILS + JSON.SUB + STORE_RATING, optional = true)
	public float storeRating;
	@JSON(key = APP_DETAILS + JSON.SUB + CATEGORY, optional = true)
	public String category;
	@JSON(key = APP_DETAILS + JSON.SUB + SUB_CATEGORY, optional = true)
	public String subCategory;
	@JSON(key = APP_DETAILS + JSON.SUB + STORE_URL, optional = true)
	public String storeUrl;

	@JSON(key = VAST, optional = true)
	private Vast[] vastAds;

	public VastAd getVastAd(Context ctx) {
		if (vastAds.length == 1) {
			String txt = vastAds[0].ad;
			try {
				Node doc = XMLSerializer.parseDocument(txt).getFirstChild();
				return new XMLSerializer<VastAd>(VastAd.class, ctx)
						.deserialize(doc);
			} catch (Exception e) {
				L.wtf(e);
			}
		}
		return null;
	}

	public int getVideoSkipTime() {
		if (vastAds.length == 1) {
			return vastAds[0].videoSkipTime;
		} else {
			return -1;
		}
	}

	public String getVideoSkipButton() {
		if (vastAds.length == 1) {
			return vastAds[0].videoSkipButton;
		} else {
			return null;
		}
	}

	public static class Vast extends Model {
		private static final long serialVersionUID = 1L;

		@JSON(key = AD)
		public String ad;
		@JSON(key = VIDEO_SKIP_TIME)
		public int videoSkipTime;
		@JSON(key = SKIP_VIDEO_BUTTON)
		public String videoSkipButton;
	}

}
