package net.pubnative.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.droidparts.persist.AbstractPrefsManager;
import org.droidparts.util.L;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class PrefsManager extends AbstractPrefsManager {

	private static final int VER = 1;

	private static final String PARAMS = "params";

	public PrefsManager(Context ctx) {
		super(ctx, VER);
	}

	public void setParams(Map<String, String> params) {
		JSONObject obj = new JSONObject();
		for (String key : params.keySet()) {
			try {
				obj.put(key, params.get(key));
			} catch (JSONException e) {
				L.w(e);
			}
		}
		saveString(PARAMS, obj.toString());
	}

	public Map<String, String> getParams() {
		HashMap<String, String> map = new HashMap<>();
		String str = getPreferences().getString(PARAMS, null);
		if (str != null) {
			try {
				JSONObject obj = new JSONObject(str);
				@SuppressWarnings("unchecked")
				Iterator<String> it = obj.keys();
				while (it.hasNext()) {
					String key = it.next();
					map.put(key, obj.getString(key));
				}
			} catch (JSONException e) {
				L.w(e);
			}
		}
		return map;
	}
}
