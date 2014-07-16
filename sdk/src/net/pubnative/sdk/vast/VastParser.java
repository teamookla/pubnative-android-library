package net.pubnative.sdk.vast;

import static org.droidparts.util.Strings.isNotEmpty;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.pubnative.sdk.vast.VastAd.Creative;
import net.pubnative.sdk.vast.VastAd.Creative.MediaFile;
import net.pubnative.sdk.vast.VastAd.Creative.TrackingEvent;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class VastParser {

	public static VastAd parse(String xml) throws Exception {
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		doc.getDocumentElement().normalize(); // ?
		//
		VastAd ad = new VastAd();
		ad.id = getLongAttribute(getFirstNodeForTag(doc, "Ad"), "id");
		ad.title = getFirstNodeText(doc, "AdTitle");
		ad.description = getFirstNodeText(doc, "Description");
		ad.impressionUrl = getFirstNodeText(doc, "Impression");
		//
		Node cn = getFirstNodeForTag(doc, "Creative");
		Creative cr = new Creative();
		ad.creatives.add(cr);
		cr.id = getLongAttribute(cn, "id");
		cr.adId = getLongAttribute(cn, "AdID");
		cr.sequesnce = (int) getLongAttribute(cn, "sequence");
		//
		for (Node ten : getNodesForTag(doc, "Tracking")) {
			TrackingEvent te = new TrackingEvent();
			cr.trackingEvents.add(te);
			te.event = getAttribute(ten, "event");
			te.url = getNodeText(ten);
		}
		//
		for (Node mfn : getNodesForTag(doc, "MediaFile")) {
			MediaFile mf = new MediaFile();
			cr.mediaFiles.add(mf);
			mf.id = getLongAttribute(mfn, "id");
			mf.height = (int) getLongAttribute(mfn, "height");
			mf.width = (int) getLongAttribute(mfn, "width");
			mf.bitrate = (int) getLongAttribute(mfn, "bitrate");
			mf.type = getAttribute(mfn, "type");
			mf.delivery = getAttribute(mfn, "delivery");
			mf.maintainAspectRatio = getBooleanAttribute(mfn,
					"maintainAspectRatio");
			mf.scalable = getBooleanAttribute(mfn, "scalable");
			mf.url = getNodeText(mfn);
		}
		return ad;
	}

	private static boolean getBooleanAttribute(Node node, String name) {
		String val = getAttribute(node, name);
		return isNotEmpty(val) ? Boolean.valueOf(val) : false;
	}

	private static long getLongAttribute(Node node, String name) {
		String val = getAttribute(node, name);
		return isNotEmpty(val) ? Long.valueOf(val) : -1;
	}

	private static String getAttribute(Node node, String name) {
		NamedNodeMap attrs = node.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (name.equals(attr.getNodeName())) {
				return attr.getNodeValue();
			}
		}
		return null;
	}

	private static String getFirstNodeText(Document doc, String tag) {
		Node n = getFirstNodeForTag(doc, tag);
		return (n != null) ? getNodeText(n) : null;
	}

	private static Node getFirstNodeForTag(Document doc, String tag) {
		List<Node> nodes = getNodesForTag(doc, tag);
		return nodes.isEmpty() ? null : nodes.get(0);
	}

	private static List<Node> getNodesForTag(Document doc, String tag) {
		NodeList nl = doc.getElementsByTagName(tag);
		return getAll(nl);
	}

	private static List<Node> getAll(NodeList nl) {
		ArrayList<Node> nodes = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			nodes.add(nl.item(i));
		}
		return nodes;
	}

	private static String getNodeText(Node n) {
		return n.getTextContent().trim();
	}

}