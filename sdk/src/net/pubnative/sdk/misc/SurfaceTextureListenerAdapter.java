package net.pubnative.sdk.misc;

import android.graphics.SurfaceTexture;
import android.view.TextureView.SurfaceTextureListener;

public abstract class SurfaceTextureListenerAdapter implements
		SurfaceTextureListener {

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
	}

}
