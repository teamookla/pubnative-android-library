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
package net.pubnative.library.util;

import org.droidparts.net.image.AbstractImageReshaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class DensityReshaper extends AbstractImageReshaper {

	private final float ratio;

	/**
	 * @see DisplayMetrics
	 */
	public DensityReshaper(Context ctx, int baseDensityDpi) {
		ratio = (float) ctx.getResources().getDisplayMetrics().densityDpi
				/ baseDensityDpi;
	}

	@Override
	public Bitmap reshape(Bitmap bm) {
		int w = (int) (bm.getWidth() * ratio);
		int h = (int) (bm.getHeight() * ratio);
		return Bitmap.createScaledBitmap(bm, w, h, true);
	}

	@Override
	public String getCacheId() {
		return "x" + ratio;
	}

	@Override
	public int getImageHeightHint() {
		return 0;
	}

	@Override
	public int getImageWidthHint() {
		return 0;
	}

}