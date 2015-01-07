package net.pubnative.interstitials.delegate;

import static org.droidparts.util.ui.ViewUtils.setGone;
import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.util.ScreenUtil;
import net.pubnative.library.model.holder.NativeAdHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InterstitialDelegate extends AbstractDelegate {

	private NativeAdHolder[] holders;

	public InterstitialDelegate(PubNativeInterstitialsActivity act) {
		super(act, 1);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.INTERSTITIAL;
	}

	@Override
	protected String getContentLayoutName() {
		return "pn_delegate_interstitial";
	}

	@Override
	public NativeAdHolder[] getNativeAdHolders() {
		return holders;
	}

	private LinearLayout linearLayout;
	private ImageView landscapeImageView, portraitGameImageView;

	@Override
	public void onCreate() {
		super.onCreate();
		linearLayout = findViewById("view_holder");
		landscapeImageView = findViewById("view_game_image");
		portraitGameImageView = findViewById("view_game_image_portrait");
		holderView.setOnClickListener(this);
		createHolders();
		applyOrientation();
	}

	private void createHolders() {
		NativeAdHolder holder = new NativeAdHolder(holderView);
		holder.iconViewId = R.id.view_icon;
		holder.bannerViewId = R.id.view_game_image;
		holder.portraitBannerViewId = R.id.view_game_image_portrait;
		holder.titleViewId = R.id.view_title;
		holder.ratingViewId = R.id.view_rating;
		holder.descriptionViewId = R.id.view_description;
		holder.downloadViewId = R.id.btn_download;
		holders = new NativeAdHolder[] { holder };
	}

	@Override
	public void onRotate() {
		super.onRotate();
		applyOrientation();
	}

	@Override
	public void onClick(View v) {
		if (v == holderView) {
			showInPlayStore(holders[0].ad);
		} else {
			super.onClick(v);
		}
	}

	private void applyOrientation() {
		boolean port = ScreenUtil.isPortrait(act);
		linearLayout.setOrientation(port ? LinearLayout.VERTICAL
				: LinearLayout.HORIZONTAL);
		setGone(port, portraitGameImageView);
		setGone(!port, landscapeImageView);
	}

}
