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
package net.pubnative.sdk.model.response;

import net.pubnative.sdk.Contract;

import org.droidparts.annotation.json.Key;

public class NativeAd extends Ad implements Contract.Response.NativeFormat {
	private static final long serialVersionUID = 1L;

	@Key(name = TITLE)
	public String title;

	@Key(name = DESCRIPTION)
	public String description;

	@Key(name = ICON_URL)
	public String iconUrl;

	@Key(name = BANNER_URL)
	public String bannerUrl;

	@Key(name = CTA_TEXT)
	public String ctaText;

	@Key(name = APP_DETAILS + Key.SUB + NAME)
	public String name;
	@Key(name = APP_DETAILS + Key.SUB + PLATFORM)
	public String platform;
	@Key(name = APP_DETAILS + Key.SUB + REVIEW)
	public String review;
	@Key(name = APP_DETAILS + Key.SUB + REVIEW_PROS)
	public String[] reviewPros;
	@Key(name = APP_DETAILS + Key.SUB + REVIEW_CONS)
	public String[] reviewCons;
	@Key(name = APP_DETAILS + Key.SUB + PUBLISHER)
	public String publisher;
	@Key(name = APP_DETAILS + Key.SUB + DEVELOPER)
	public String developer;
	@Key(name = APP_DETAILS + Key.SUB + VERSION)
	public String version;
	@Key(name = APP_DETAILS + Key.SUB + SIZE)
	public String size;
	@Key(name = APP_DETAILS + Key.SUB + AGE_RATING)
	public String ageRating;
	@Key(name = APP_DETAILS + Key.SUB + STORE_RATING)
	public float storeRating;
	@Key(name = APP_DETAILS + Key.SUB + CATEGORY)
	public String category;
	@Key(name = APP_DETAILS + Key.SUB + SUB_CATEGORY)
	public String subCategory;
	@Key(name = APP_DETAILS + Key.SUB + STORE_URL)
	public String storeUrl;
	@Key(name = APP_DETAILS + Key.SUB + STORE_ID)
	public String storeId;

}
