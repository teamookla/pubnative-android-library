package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.adapter.NativeAdHolderAdapter;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.sdk.model.AdFormat;
import net.pubnative.sdk.model.holder.NativeAdHolder;
import net.pubnative.sdk.model.request.AdRequest;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NativeDelegate extends AbstractDelegate implements
		OnItemClickListener {

	private static final int COUNT = 5;

	private ListView listView;

	private NativeAdHolder[] holders;
	private NativeAdHolderAdapter adapter;

	public NativeDelegate(PubNativeInterstitialsActivity act) {
		super(act);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.NATIVE;
	}

	@Override
	protected String getContentLayoutName() {
		return "pn_delegate_native";
	}

	@Override
	public AdRequest getAdRequest(String appKey) {
		AdRequest req = new AdRequest(appKey, AdFormat.NATIVE);
		req.setAdCount(COUNT);
		req.fillInDefaults(act);
		return req;
	}

	@Override
	public NativeAdHolder[] getNativeAdHolders() {
		return holders;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listView = findViewById("view_list");
		adapter = new NativeAdHolderAdapter(act);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
		createHolders();
	}

	private void createHolders() {
		holders = new NativeAdHolder[COUNT];
		for (int i = 0; i < COUNT; i++) {
			holders[i] = adapter.makeAndAddHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showInPlayStore(adapter.getItem(position).ad);
	}

}
