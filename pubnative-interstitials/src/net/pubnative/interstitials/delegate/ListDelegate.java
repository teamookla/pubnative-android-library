package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.delegate.adapter.GameListAdapter;
import net.pubnative.interstitials.delegate.adapter.NativeAdHolderAdapter;
import android.content.Context;

public class ListDelegate extends NativeDelegate {

	public ListDelegate(PubNativeInterstitialsActivity act, int adCount) {
		super(act, adCount);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.LIST;
	}

	@Override
	protected NativeAdHolderAdapter createAdapter(Context ctx) {
		return new GameListAdapter(ctx);
	}

}