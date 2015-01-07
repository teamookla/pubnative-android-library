package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.widget.AdCarouselView;
import net.pubnative.library.model.holder.NativeAdHolder;

import org.droidparts.adapter.widget.ArrayAdapter;
import org.droidparts.util.ui.ViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CarouselDelegate extends AbstractDelegate implements
		AdCarouselView.Listener {

	private ListView listView;
	private AdCarouselView carouselView;
	private NativeAdHolder[] holders;

	public CarouselDelegate(PubNativeInterstitialsActivity act, int adCount) {
		super(act, adCount);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listView = ViewUtils.findViewById(act, R.id.view_list);
		listView.setAdapter(new ListAdapter(act));
		carouselView = new AdCarouselView(act);
		carouselView.setListener(this);
		holders = carouselView.createAndAddHolders(adCount);
	}

	@Override
	public NativeAdHolder[] getNativeAdHolders() {
		return holders;
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.CAROUSEL;
	}

	@Override
	protected String getContentLayoutName() {
		return "pn_delegate_list";
	}

	@Override
	public void didClick(NativeAdHolder holder) {
		showInPlayStore(holder.ad);
	}

	private class ListAdapter extends ArrayAdapter<String> {

		public ListAdapter(Context ctx) {
			super(ctx);
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (position != 3) {
				view = LayoutInflater.from(getContext()).inflate(
						android.R.layout.simple_list_item_1, null);
				TextView tv = ViewUtils.findViewById(view, android.R.id.text1);
				tv.setText("Row " + position);
				return view;
			} else {
				return carouselView;
			}
		}
	}

}
