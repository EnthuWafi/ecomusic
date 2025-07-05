package com.enth.ecomusic.util;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class NumberFormatUtil {
	
	private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
	static {
	  suffixes.put(1_000L, "k");
	  suffixes.put(1_000_000L, "M");
	  suffixes.put(1_000_000_000L, "G");
	  suffixes.put(1_000_000_000_000L, "T");
	  suffixes.put(1_000_000_000_000_000L, "P");
	  suffixes.put(1_000_000_000_000_000_000L, "E");
	}
	private NumberFormatUtil() {}
	
	

	public static String format(long value) {
	  //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
	  if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
	  if (value < 0) return "-" + format(-value);
	  if (value < 1000) return Long.toString(value); //deal with easy case

	  Entry<Long, String> e = suffixes.floorEntry(value);
	  Long divideBy = e.getKey();
	  String suffix = e.getValue();

	  long truncated = value / (divideBy / 10); //the number part of the output times 10
	  boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
	  return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
	
	public static String formatDuration(long millis) {
	    long hours = TimeUnit.MILLISECONDS.toHours(millis);
	    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
	    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

	    StringBuilder result = new StringBuilder();
	    if (hours > 0) {
	        result.append(hours).append(" hr");
	    }
	    if (minutes > 0) {
	        if (result.length() > 0) result.append(", ");
	        result.append(minutes).append(" min");
	    }
	    if (seconds > 0 || result.length() == 0) { // always show at least seconds
	        if (result.length() > 0) result.append(", ");
	        result.append(seconds).append(" sec");
	    }

	    return result.toString();
	}

}
