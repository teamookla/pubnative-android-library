package net.pubnative.interstitials.widget;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import net.pubnative.interstitials.R;
import net.pubnative.library.model.holder.NativeAdHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class AdCarouselView extends HorizontalScrollView implements
		View.OnClickListener {

	public interface Listener {
		void didClick(NativeAdHolder holder);
	}

	private final LayoutInflater layoutInflater;

	private final LinearLayout containerView;

	private NativeAdHolder[] holders;

	private Listener listener;

	public AdCarouselView(Context context) {
		this(context, null);
	}

	public AdCarouselView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		layoutInflater = LayoutInflater.from(ctx);
		containerView = new LinearLayout(ctx);
		setHorizontalScrollBarEnabled(false);
		setOverScrollMode(OVER_SCROLL_NEVER);
		addView(containerView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
		gd = new GestureDetector(ctx, gl, null);
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		for (int pos = 0; pos < containerView.getChildCount(); pos++) {
			View thumbView = containerView.getChildAt(pos);
			if (v == thumbView) {
				NativeAdHolder holder = holders[pos];
				if (listener != null) {
					listener.didClick(holder);
				}
				break;
			}
		}
	}

	public NativeAdHolder[] createAndAddHolders(int count) {
		holders = new NativeAdHolder[count];
		containerView.removeAllViews();
		for (int idx = 0; idx < count; idx++) {
			View view = layoutInflater.inflate(R.layout.pn_view_carousel_item,
					null);
			view.setOnClickListener(this);
			containerView.addView(view, new LayoutParams(WRAP_CONTENT,
					WRAP_CONTENT));
			//
			NativeAdHolder holder = new NativeAdHolder(view);
			holder.bannerViewId = R.id.view_banner;
			holder.iconViewId = R.id.view_icon;
			holder.titleViewId = R.id.view_title;
			holder.ratingViewId = R.id.view_rating;
			holder.downloadViewId = R.id.view_download;
			holders[idx] = holder;
		}
		requestLayout();
		return holders;
	}

	//

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (gd.onTouchEvent(ev)) {
			return true;
		} else {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (firstTouchX == -1) {
					firstTouchX = (int) ev.getRawX();
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				swipeLength = (int) (firstTouchX - ev.getRawX());
				firstTouchX = -1;
				setCurrHolder();
				scrollToCurrHolder();
				return true;
			}
			return super.onTouchEvent(ev);
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		postDelayed(new Runnable() {

			@Override
			public void run() {
				scrollToCurrHolder();
			}
		}, 100);
	}

	private int firstTouchX = -1;
	private int swipeLength;

	private int currHolder = 0;

	private void setCurrHolder() {
		int scrollPosition = getScrollX();
		int screenWidth = getMeasuredWidth();
		int holderWidth = containerView.getChildAt(0).getMeasuredWidth();
		int lastHolder = containerView.getChildCount() - 1;
		int containerWidth = containerView.getMeasuredWidth();
		boolean screenEnd = (scrollPosition == (containerWidth - screenWidth));
		if (screenEnd) {
			currHolder = lastHolder;
		} else {
			int step = (swipeLength > 0) ? holderWidth : holderWidth / 2;
			currHolder = (scrollPosition + step) / holderWidth;
		}
	}

	private void scrollToCurrHolder() {
		int screenWidth = getMeasuredWidth();
		int holderWidth = containerView.getChildAt(0).getMeasuredWidth();
		int lastHolder = containerView.getChildCount() - 1;
		int containerWidth = containerView.getMeasuredWidth();
		int scrollTo;
		if (currHolder <= 0) {
			currHolder = 0;
			scrollTo = 0;
		} else if (currHolder >= lastHolder) {
			currHolder = lastHolder;
			scrollTo = containerWidth - holderWidth;
		} else {
			scrollTo = currHolder * holderWidth - (screenWidth - holderWidth)
					/ 2;
		}
		smoothScrollTo(scrollTo, 0);
	}

	private GestureDetector gd;
	private final GestureDetector.OnGestureListener gl = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean right = (velocityX < 0);
			if (right) {
				currHolder++;
			} else {
				currHolder--;
			}
			scrollToCurrHolder();
			return true;
		}
	};

}
