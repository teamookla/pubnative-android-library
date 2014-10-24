package net.pubnative.interstitials.tester;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.droidparts.util.Strings;

public class Logger {

	private static final DateFormat DF = new SimpleDateFormat("HH:mm:ss:SSS");

	private final ArrayList<String> records = new ArrayList<String>();

	public void append(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(DF.format(new Date()));
		sb.append("  ");
		sb.append(msg);
		records.add(sb.toString());
	}

	public String getRecords() {
		return Strings.join(records, "\n");
	}

	public void clear() {
		records.clear();
	}

}
