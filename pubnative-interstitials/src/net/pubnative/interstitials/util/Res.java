package net.pubnative.interstitials.util;

import java.util.concurrent.ConcurrentHashMap;

import org.droidparts.util.ui.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

// access resources this way because Unity regenerates R.java
// http://forum.unity3d.com/threads/139741-Problem-with-R-class-in-custom-unity-player-activity
public class Res {

	// TODO move somewhere else

	public static final <T extends View> T inflate(Context ctx, String id) {
		return inflate(ctx, id, null);
	}

	@SuppressWarnings("unchecked")
	public static final <T extends View> T inflate(Context ctx, String id,
			ViewGroup root) {
		return (T) LayoutInflater.from(ctx).inflate(layoutId(ctx, id), root);
	}

	public static final <T extends View> T findViewById(Activity act, String id) {
		return ViewUtils.findViewById(act, id(act, id));
	}

	@SuppressWarnings("unchecked")
	public static final <T extends View> T findViewById(View view, String id) {
		return (T) view.findViewById(id(view.getContext(), id));
	}

	public static Animation animation(Context ctx, String name) {
		return AnimationUtils.loadAnimation(ctx, animId(ctx, name));
	}

	// TODO end

	public static boolean bool(Context ctx, String name) {
		return ctx.getResources().getBoolean(getResIdByName(ctx, "bool", name));
	}

	public static int integer(Context ctx, String name) {
		return ctx.getResources().getInteger(
				getResIdByName(ctx, "integer", name));
	}

	public static String string(Context ctx, String name) {
		return ctx.getResources()
				.getString(getResIdByName(ctx, "string", name));
	}

	public static int dpPxSize(Context ctx, String name) {
		int id = dimenId(ctx, name);
		return ctx.getResources().getDimensionPixelSize(id);
	}

	//

	public static int id(Context ctx, String name) {
		return getResIdByName(ctx, "id", name);
	}

	private static int dimenId(Context ctx, String name) {
		return getResIdByName(ctx, "dimen", name);
	}

	public static int colorId(Context ctx, String name) {
		return getResIdByName(ctx, "color", name);
	}

	public static int drawableId(Context ctx, String name) {
		return getResIdByName(ctx, "drawable", name);
	}

	public static int layoutId(Context ctx, String name) {
		return getResIdByName(ctx, "layout", name);
	}

	public static int rawId(Context ctx, String name) {
		return getResIdByName(ctx, "raw", name);
	}

	private static int animId(Context ctx, String name) {
		return getResIdByName(ctx, "anim", name);
	}

	private static int getResIdByName(Context ctx, String type, String name) {
		String key = (type + name);
		Integer id = map.get(key);
		if (id == null) {
			id = ctx.getResources().getIdentifier(name, type,
					ctx.getPackageName());
			map.put(key, id);
		}
		return id;
	}

	private static final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

	private Res() {
	}

}
