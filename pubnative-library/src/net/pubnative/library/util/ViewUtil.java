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

import net.pubnative.library.misc.SurfaceTextureListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {

	public static int getVisiblePercent(View v) {
		if (v.isShown()) {
			Rect r = new Rect();
			v.getGlobalVisibleRect(r);
			double sVisible = r.width() * r.height();
			double sTotal = v.getWidth() * v.getHeight();
			return (int) (100 * sVisible / sTotal);
		} else {
			return -1;
		}
	}

	public static Point getFullSceeenSize(Context ctx, MediaPlayer mediaPlayer) {
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

		int videoW = mediaPlayer.getVideoWidth();
		int videoH = mediaPlayer.getVideoHeight();

		float screenW = dm.widthPixels;
		float screenH = dm.heightPixels;

		float scale = Math.min(screenW / videoW, screenH / videoH);

		Point p = new Point();
		p.x = (int) (videoW * scale);
		p.y = (int) (videoH * scale);
		return p;
	}

	public static void setSurface(final MediaPlayer mp, final TextureView tv) {
		SurfaceTexture st = tv.getSurfaceTexture();
		if (st != null) {
			mp.setSurface(new Surface(st));
		} else {
			tv.setSurfaceTextureListener(new SurfaceTextureListenerAdapter() {

				@Override
				public void onSurfaceTextureAvailable(SurfaceTexture st,
						int width, int height) {
					tv.setSurfaceTextureListener(null);
					mp.setSurface(new Surface(st));
				}
			});
		}
	}

	public static void setSize(TextureView tv, int w, int h) {
		ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
		layoutParams.width = w;
		layoutParams.height = h;
		tv.setLayoutParams(layoutParams);
	}

}
