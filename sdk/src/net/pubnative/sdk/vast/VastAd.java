package net.pubnative.sdk.vast;

import java.util.ArrayList;

import net.pubnative.sdk.vast.VastAd.Creative.TrackingEvent;

import org.droidparts.annotation.serialize.XML;
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

	private static final String BASE = "Ad" + XML.SUB + "InLine" + XML.SUB;

	@XML(tag = "Ad", attribute = "id")
	public long id;
	@XML(tag = BASE + "AdTitle")
	public String title;
	@XML(tag = BASE + "Description")
	public String description;
	@XML(tag = BASE + "Impression")
	public String impressionUrl;

	@XML(tag = BASE + "Creatives", attribute = "Creative")
	public ArrayList<Creative> creatives;

	public static class Creative extends Model {
		private static final long serialVersionUID = 1L;

		@XML(attribute = "id")
		public long id;
		@XML(attribute = "AdID")
		public long adId;
		@XML(attribute = "sequence")
		public int sequesnce;
		@XML(tag = "Linear" + XML.SUB + "Duration")
		public String duration;

		@XML(tag = "Linear" + XML.SUB + "VideoClicks" + XML.SUB
				+ "ClickThrough")
		public String videoClickUrl;

		@XML(tag = "Linear" + XML.SUB + "TrackingEvents", attribute = "Tracking")
		public ArrayList<TrackingEvent> trackingEvents;
		@XML(tag = "Linear" + XML.SUB + "MediaFiles", attribute = "MediaFile")
		public ArrayList<MediaFile> mediaFiles;

		//

		public static class TrackingEvent extends Model {
			private static final long serialVersionUID = 1L;

			@XML(attribute = "event")
			public String event;
			@XML
			public String url;
		}

		public static class MediaFile extends Model {
			private static final long serialVersionUID = 1L;

			@XML(attribute = "id")
			public long id;
			@XML(attribute = "bitrate")
			public int bitrate;
			@XML(attribute = "delivery")
			public String delivery;
			@XML(attribute = "height")
			public int height;
			@XML(attribute = "maintainAspectRatio")
			public boolean maintainAspectRatio;
			@XML(attribute = "scalable")
			public boolean scalable;
			@XML(attribute = "type")
			public String type;
			@XML(attribute = "width")
			public int width;

			@XML
			public String url;
		}
	}

}
