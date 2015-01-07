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
package net.pubnative.library;

public interface PubNativeContract {

	String BASE_URL = "http://api.pubnative.net/api/partner/v2/promotions/";

	String NATIVE = "native";
	String IMAGE = "image";

	interface RequestInfo {

		String APP_TOKEN = "app_token";
		String BUNDLE_ID = "bundle_id";
		String ZONE_ID = "zone_id";
		String AD_COUNT = "ad_count";
		String USER_AGENT = "user_agent";
		String OS = "os";
		String OS_VERSION = "os_version";
		String DEVICE_MODEL = "device_model";
		String LOCALE = "locale";
		String ICON_SIZE = "icon_size";
		String BANNER_SIZE = "banner_size";
		String PORTRAIT_BANNER_SIZE = "portrait_banner_size";
		String DEVICE_RESOLUTION = "device_resolution";
		String DEVICE_TYPE = "device_type";
		String LAT = "lat";
		String LONG = "long";
		String GENDER = "gender";
		String AGE = "age";
		String KEYWORDS = "keywords";
		String PARTNER = "partner";
	}

	interface UserIdentifier {

		String NO_USER_ID = "no_user_id";
		String APPLE_IDFA = "apple_idfa";
		String APPLE_IDFA_SHA1 = "apple_idfa_sha1";
		String APPLE_IDFA_MD5 = "apple_idfa_md5";
		String ANDROID_ID = "android_id";
		String ANDROID_ID_SHA1 = "android_id_sha1";
		String ANDROID_ID_MD5 = "android_id_md5";
		String ANDROID_IMEI = "android_imei";
		String ANDROID_IMEI_SHA1 = "android_imei_sha1";
		String ANDROID_IMEI_MD5 = "android_imei_md5";
		String ANDROID_ADVERTISER_ID = "android_advertiser_id";
		String ANDROID_ADVERTISER_ID_SHA1 = "android_advertiser_id_sha1";
		String ANDROID_ADVERTISER_ID_MD5 = "android_advertiser_id_md5";
		String MAC_ADDRESS = "mac_address";
		String MAC_ADDRESS_SHA1 = "mac_address_sha1";
		String MAC_ADDRESS_MD5 = "mac_address_md5";
	}

	interface Response {

		String ADS = "ads";

		// String STATUS = "status";

		interface Format {

			String TYPE = "type";
			//
			String CLICK_URL = "click_url";
			//
			String BEACONS = "beacons";
			String URL = "url";

			String STORE_ID = "store_id";
		}

		interface NativeFormat extends Format {

			String TITLE = "title";
			String DESCRIPTION = "description";

			String ICON_URL = "icon_url";
			String BANNER_URL = "banner_url";
			String PORTRAIT_BANNER_URL = "portrait_banner_url";

			String VIDEO_URL = "video_url";

			String CTA_TEXT = "cta_text";

			String APP_DETAILS = "app_details";
			String NAME = "name";
			String PLATFORM = "platform";
			String REVIEW = "review";
			String REVIEW_PROS = "review_pros";
			String REVIEW_CONS = "review_cons";
			String PUBLISHER = "publisher";
			String DEVELOPER = "developer";
			String VERSION = "version";
			String SIZE = "size";
			String AGE_RATING = "age_rating";
			String STORE_RATING = "store_rating";
			String CATEGORY = "category";
			String SUB_CATEGORY = "sub_category";
			String STORE_URL = "store_url";
		}

		interface ImageFormat extends Format {

			String WIDTH = "width";
			String HEIGHT = "height";
			String IMAGE_URL = "image_url";
		}

	}

}
