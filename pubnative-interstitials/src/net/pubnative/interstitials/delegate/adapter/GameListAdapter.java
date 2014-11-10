package net.pubnative.interstitials.delegate.adapter;

import net.pubnative.interstitials.R;
import android.content.Context;

public class GameListAdapter extends NativeAdHolderAdapter {

	public GameListAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.pn_view_row_list_delegate;
	}

}
