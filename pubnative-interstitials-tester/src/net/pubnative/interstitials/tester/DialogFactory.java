package net.pubnative.interstitials.tester;

import static org.droidparts.util.ui.ViewUtils.findViewById;

import org.droidparts.util.ui.AbstractDialogFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

class DialogFactory extends AbstractDialogFactory {

	public DialogFactory(Context ctx) {
		super(ctx);
	}

	public static interface SettingsDialogListener {

		void onAdCountChanged(int count);

	}

	public AlertDialog getSettingsDialog(final int adCount,
			final SettingsDialogListener listener) {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.dialog_settings, null);
		//
		final TextView adCountView = findViewById(view, R.id.view_ad_count);
		adCountView.setText(String.valueOf(adCount));
		OnClickListener l = new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int newCount = Integer.parseInt(adCountView.getText().toString());
				if(adCount != newCount){
					listener.onAdCountChanged(newCount);
				}
			}};
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(view);
		builder.setNeutralButton(android.R.string.ok, l);
		return builder.create();
	}
}