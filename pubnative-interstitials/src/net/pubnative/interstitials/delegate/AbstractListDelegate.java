package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.delegate.adapter.NativeAdHolderAdapter;
import net.pubnative.library.model.holder.NativeAdHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AbstractListDelegate extends AbstractDelegate implements
		OnItemClickListener {

	private ListView listView;

	private NativeAdHolder[] holders;
	private NativeAdHolderAdapter adapter;

	public AbstractListDelegate(PubNativeInterstitialsActivity act, int adCount) {
		super(act, adCount);
	}

	@Override
	public PubNativeInterstitialsType getType() {
		return PubNativeInterstitialsType.LIST;
	}

	@Override
	protected String getContentLayoutName() {
		return "pn_delegate_list";
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
		holders = new NativeAdHolder[adCount];
		for (int i = 0; i < adCount; i++) {
			holders[i] = adapter.makeAndAddHolder();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showInPlayStore(adapter.getItem(position).ad);
	}

}
