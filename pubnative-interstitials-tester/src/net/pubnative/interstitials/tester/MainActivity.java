package net.pubnative.interstitials.tester;

import static org.droidparts.util.ui.ViewUtils.setInvisible;

import java.util.Locale;

import net.pubnative.interstitials.PubNativeInterstitials;
import net.pubnative.interstitials.api.PubNativeInterstitialsListener;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.contract.PubNativeInterstitialsConstants;
import net.pubnative.interstitials.logging.EventData;
import net.pubnative.interstitials.logging.EventLogger;
import net.pubnative.interstitials.tester.DialogFactory.SettingsDialogListener;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.activity.legacy.Activity;
import org.droidparts.annotation.inject.InjectView;
import org.droidparts.util.L;
import org.droidparts.util.Strings;
import org.droidparts.util.ui.ViewUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		PubNativeInterstitialsListener, EventLogger.Listener {

	@InjectView(id = R.id.view_action_bar)
	private View actionBarView;

	@InjectView(id = R.id.view_version)
	private TextView versionView;

	@InjectView(id = R.id.view_table)
	private TableLayout tableView;

	@InjectView(id = R.id.view_status_bar_loading)
	private ProgressBar statusBarLoadingView;
	@InjectView(id = R.id.view_log_text)
	private TextView logTextView;

	private static final Logger logger = new Logger();
	private DialogFactory dialogFactory;

	@Override
	public void onPreInject() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialogFactory = new DialogFactory(this);
		EventLogger.setListener(this);
		versionView.setText(PubNativeInterstitialsConstants.VERSION);
		actionBarView.setOnClickListener(this);
		addRows();
		PubNativeInterstitials.init(this, Contract.APP_TOKEN);
		PubNativeInterstitials.addListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		showLoading(false);
	}

	@Override
	public void onClick(View v) {
		if (v == actionBarView) {
			dialogFactory.getSettingsDialog(adCount, l).show();
		} else {
			onRowButtonClick(v);
		}
	}

	private final SettingsDialogListener l = new SettingsDialogListener() {

		@Override
		public void onAdCountChanged(int count) {
			adCount = count;
		}

	};

	private int adCount = 5;

	private void onRowButtonClick(View v) {
		String tag = (String) v.getTag();
		PubNativeInterstitialsType type = null;
		try {
			type = PubNativeInterstitialsType.valueOf(tag);
		} catch (Exception ignored) {
		}
		showLoading(true);
		PubNativeInterstitials.show(this, type, adCount);
	}

	//

	@Override
	public void onShown(PubNativeInterstitialsType type) {
		showLoading(false);
		L.i("Shown %s.", type);
	}

	@Override
	public void onTapped(NativeAd ad) {
		L.i("Tapped %s.", ad);
	}

	@Override
	public void onClosed(PubNativeInterstitialsType type) {
		L.i("Closed %s", type);
	}

	@Override
	public void onError(Exception ex) {
		L.w(ex);
		showLoading(false);
	}

	private void showLoading(boolean loading) {
		setInvisible(!loading, statusBarLoadingView);
	}

	@Override
	public void onEvent(EventData data) {
		String txt = data.toString();
		L.i(txt);
		if (txt.length() > 512) {
			txt = txt.substring(0, 512) + " ...";
		}
		logger.append(txt);
		logTextView.setText(logger.getRecords());
	}

	//

	private void addRows() {
		for (PubNativeInterstitialsType type : PubNativeInterstitialsType
				.values()) {
			addRow(type.name());
		}
	}

	private void addRow(String name) {
		View rowView = LayoutInflater.from(this).inflate(
				R.layout.view_table_row, null);
		TextView titleView = ViewUtils.findViewById(rowView, R.id.view_title);
		Button showBtn = ViewUtils.findViewById(rowView, R.id.btn_show);

		titleView.setText(toHumanReadable(name));
		showBtn.setTag(name);
		showBtn.setOnClickListener(this);

		tableView.addView(rowView);
	}

	private static String toHumanReadable(String name) {
		String[] parts = name.split("_");
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			part = part.substring(0, 1).toUpperCase(Locale.US)
					+ part.substring(1).toLowerCase(Locale.US);
			parts[i] = part;
		}
		return Strings.join(parts, " ", ":");
	}
}
