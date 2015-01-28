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
package net.pubnative.library.inner;

import net.pubnative.library.model.holder.AdHolder;
import net.pubnative.library.model.holder.VideoAdHolder;
import android.content.Context;
import android.media.MediaPlayer;

public class WorkerItem<T extends AdHolder<?>> {

	public final T holder;

	public long firstAppeared = -1;
	public boolean confirmed;
	//
	public MediaPlayer mp;
	public boolean preparing;
	public boolean prepared;
	public boolean played;

	WorkerItem(T holder) {
		this.holder = holder;
	}

	boolean isPlaying() {
		return (mp != null && mp.isPlaying());
	}

	boolean inFeedVideo() {
		if (holder instanceof VideoAdHolder) {
			return (((VideoAdHolder) holder).playButtonViewId) <= 0;
		}
		return false;
	}

	public Context getContext() {
		return holder.getView().getContext();
	}

	@Override
	public boolean equals(Object o) {
		return holder.equals(o);
	}

	@Override
	public int hashCode() {
		return holder.hashCode();
	}

}