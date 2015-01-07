package net.pubnative.library.widget;

import static org.droidparts.util.ui.ViewUtils.findViewById;
import net.pubnative.library.R;
import net.pubnative.library.util.ViewUtil;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class VideoPopup extends PopupWindow {

	private final MediaPlayer mp;
	private final TextureView parentTv;

	private final TextureView tv;

	public VideoPopup(MediaPlayer mp, TextureView parentTv) {
		this.mp = mp;
		this.parentTv = parentTv;
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		setBackgroundDrawable(new ColorDrawable());
		setContentView(LayoutInflater.from(parentTv.getContext()).inflate(
				R.layout.pubnative_video_popup, null));
		tv = findViewById(getContentView(), R.id.view_texture);
	}

	public void show() {
		showAtLocation(parentTv, Gravity.CENTER, 0, 0);
		ViewUtil.setSurface(mp, tv);
		Point p = ViewUtil.getFullSceeenSize(tv.getContext(), mp);
		ViewUtil.setSize(tv, p.x, p.y);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		ViewUtil.setSurface(mp, parentTv);
	}

}
