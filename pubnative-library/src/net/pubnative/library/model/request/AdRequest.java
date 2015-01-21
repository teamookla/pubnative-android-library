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
package net.pubnative.library.model.request;

import java.io.Serializable;
import java.util.HashMap;

import net.pubnative.library.PubNativeContract;
import net.pubnative.library.PubNativeContract.RequestInfo;
import net.pubnative.library.model.AdFormat;
import net.pubnative.library.util.KeyUtil;
import android.content.Context;
import android.net.Uri;

public class AdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String appToken;
	private final AdFormat format;
	private final HashMap<String, String> params = new HashMap<String, String>();

	public AdRequest(String appToken, AdFormat format) {
		this.appToken = appToken;
		this.format = format;
		params.put(RequestInfo.APP_TOKEN, appToken);
	}

	public void fillInDefaults(Context ctx) {
		for (String key : KeyUtil.getAdRequestKeys()) {
			String val = KeyUtil.putDefaultVal(ctx, key);
			if (val != null) {
				params.put(key, val);
			}
		}
	}

	public void setAdCount(int count) {
		params.put(RequestInfo.AD_COUNT, String.valueOf(count));
	}

	public void setIconSize(int w, int h) {
		params.put(RequestInfo.ICON_SIZE, w + "x" + h);
	}

	public void setBannerSize(int w, int h) {
		params.put(RequestInfo.BANNER_SIZE, w + "x" + h);
	}

	public Uri buildUri() {
		Uri.Builder bldr = Uri.parse(PubNativeContract.BASE_URL).buildUpon();
		switch (format) {
		case NATIVE:
			bldr.appendPath(PubNativeContract.NATIVE);
			break;
		case IMAGE:
			bldr.appendPath(PubNativeContract.IMAGE);
			break;
		default:
			throw new IllegalArgumentException(format.toString());
		}
		params.put(RequestInfo.APP_TOKEN, appToken);
		for (String key : params.keySet()) {
			bldr.appendQueryParameter(key, params.get(key));
		}
		return bldr.build();
	}

	public AdFormat getAdFormat() {
		return format;
	}

	public void setParam(String key, String val) {
		if (val == null) {
			params.remove(key);
		} else {
			params.put(key, val);
		}
	}

	public String getParam(String key) {
		return params.get(key);
	}

	public HashMap<String, String> getParams() {
		return params;
	}

}
