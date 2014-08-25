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

import net.pubnative.sdk.PubNativeContract;

import org.droidparts.annotation.serialize.JSON;

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

	@JSON(key = VIDEO_URL)
	public String videoUrl;

	@JSON(key = CTA_TEXT)
	public String ctaText;

	@JSON(key = APP_DETAILS + JSON.SUB + NAME)
	public String name;
	@JSON(key = APP_DETAILS + JSON.SUB + PLATFORM)
	public String platform;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW)
	public String review;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW_PROS)
	public String[] reviewPros;
	@JSON(key = APP_DETAILS + JSON.SUB + REVIEW_CONS)
	public String[] reviewCons;
	@JSON(key = APP_DETAILS + JSON.SUB + PUBLISHER)
	public String publisher;
	@JSON(key = APP_DETAILS + JSON.SUB + DEVELOPER)
	public String developer;
	@JSON(key = APP_DETAILS + JSON.SUB + VERSION)
	public String version;
	@JSON(key = APP_DETAILS + JSON.SUB + SIZE)
	public String size;
	@JSON(key = APP_DETAILS + JSON.SUB + AGE_RATING)
	public String ageRating;
	@JSON(key = APP_DETAILS + JSON.SUB + STORE_RATING)
	public float storeRating;
	@JSON(key = APP_DETAILS + JSON.SUB + CATEGORY)
	public String category;
	@JSON(key = APP_DETAILS + JSON.SUB + SUB_CATEGORY)
	public String subCategory;
	@JSON(key = APP_DETAILS + JSON.SUB + STORE_URL)
	public String storeUrl;
	@JSON(key = APP_DETAILS + JSON.SUB + STORE_ID)
	public String storeId;

}
