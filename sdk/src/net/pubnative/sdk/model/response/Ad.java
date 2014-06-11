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
import org.droidparts.model.Model;

public abstract class Ad extends Model implements
		Contract.Response.Format {
	private static final long serialVersionUID = 1L;

	@Key(name = TYPE)
	public String type;

	@Key(name = CLICK_URL)
	public String clickUrl;

	@Key(name = BEACONS)
	private Beacon[] beacons;

	public String getConfirmationUrl() {
		return (beacons.length == 1) ? beacons[0].url : null;
	}

	public static class Beacon extends Model {
		private static final long serialVersionUID = 1L;

		@Key(name = TYPE)
		public String type;
		@Key(name = URL)
		public String url;
	}

}
