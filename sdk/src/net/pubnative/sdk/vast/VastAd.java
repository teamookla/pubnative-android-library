package net.pubnative.sdk.vast;

import java.util.ArrayList;

import net.pubnative.sdk.vast.VastAd.Creative.TrackingEvent;

import org.droidparts.annotation.json.Key;
import org.droidparts.model.Model;

public class VastAd extends Model {
	private static final long serialVersionUID = 1L;

	//

	public String getVideoUrl() {
		return creatives.get(0).mediaFiles.get(0).url;
	}

	public String getImpressionUrl() {
		return impressionUrl;
	}

	public String getEventUrl(VastEvent ev) {
		Creative cr = creatives.get(0);
		for (TrackingEvent te : cr.trackingEvents) {
			if (ev.key.equals(te.event)) {
				return te.url;
			}
		}
		return null;
	}

	//

	@Key
	public long id;
	@Key
	public String title;
	@Key
	public String description;
	@Key
	public String impressionUrl;

	public final ArrayList<Creative> creatives = new ArrayList<>();

	public static class Creative extends Model {
		private static final long serialVersionUID = 1L;

		@Key
		public long id;
		@Key
		public long adId;
		@Key
		public int sequesnce;

		@Key
		public final ArrayList<TrackingEvent> trackingEvents = new ArrayList<>();
		@Key
		public final ArrayList<MediaFile> mediaFiles = new ArrayList<>();

		//

		public static class TrackingEvent extends Model {
			private static final long serialVersionUID = 1L;
			@Key
			public String event;
			@Key
			public String url;
		}

		public static class MediaFile extends Model {
			private static final long serialVersionUID = 1L;

			@Key
			public long id;
			@Key
			public int bitrate;
			@Key
			public String delivery;
			@Key
			public int height;
			@Key
			public boolean maintainAspectRatio;
			@Key
			public boolean scalable;
			@Key
			public String type;
			@Key
			public int width;

			@Key
			public String url;
		}
	}

}
