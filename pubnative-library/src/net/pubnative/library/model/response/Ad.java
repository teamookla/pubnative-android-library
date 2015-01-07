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

import org.droidparts.annotation.serialize.JSON;
import org.droidparts.model.Model;

public abstract class Ad extends Model implements
		PubNativeContract.Response.Format {
	private static final long serialVersionUID = 1L;

	@JSON(key = TYPE)
	public String type;

	@JSON(key = CLICK_URL)
	public String clickUrl;

	@JSON(key = BEACONS)
	private Beacon[] beacons;

	public String getPackageName() {
		if (this instanceof NativeAd) {
			return ((NativeAd) this).storeId;
		} else if (this instanceof ImageAd) {
			return ((ImageAd) this).storeId;
		} else {
			return null;
		}
	}

	public String getConfirmationUrl() {
		return (beacons.length == 1) ? beacons[0].url : null;
	}

	public static class Beacon extends Model {
		private static final long serialVersionUID = 1L;

		@JSON(key = TYPE)
		public String type;
		@JSON(key = URL)
		public String url;
	}

}
