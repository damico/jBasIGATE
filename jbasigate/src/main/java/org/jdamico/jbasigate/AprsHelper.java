package org.jdamico.jbasigate;

import java.util.Calendar;
import java.util.Date;

public class AprsHelper {
	
	private String getDMS(double decimalDegree, boolean isLatitude) {
		Integer positionAmbiguity = 0;
		int minFrac = (int)Math.round(decimalDegree*6000); ///< degree in 1/100s of a minute
		boolean negative = (minFrac < 0);
		if (negative)
			minFrac = -minFrac;
		int deg = minFrac / 6000;
		int min = (minFrac / 100) % 60;
		minFrac = minFrac % 100;
		String ambiguousFrac;

		switch (positionAmbiguity) {
		case 1: // "dd  .  N"
			ambiguousFrac = "  .  "; break;
		case 2: // "ddm .  N"
			ambiguousFrac = String.format("%d .  ", min/10); break;
		case 3: // "ddmm.  N"
			ambiguousFrac = String.format("%02d.  ", min); break;
		case 4: // "ddmm.f N"
			ambiguousFrac = String.format("%02d.%d ", min, minFrac/10); break;
		default: // "ddmm.ffN"
			ambiguousFrac = String.format("%02d.%02d", min, minFrac); break;
		}
		if ( isLatitude ) {
			return String.format("%02d%s%s", deg, ambiguousFrac, ( negative ? "S" : "N"));
		} else {
			return String.format("%03d%s%s", deg, ambiguousFrac, ( negative ? "W" : "E"));
		}
	}

	public String getLatLonAprs(double latitude, double longitude) {
		char symbolTable = '/';
		return getDMS(latitude,true)+symbolTable+getDMS(longitude,false);
	}
	
	public String getDateTimeLocMsg(Date now, int tz, double latitude, double longitude) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int calMinute = cal.get(Calendar.MINUTE);
		cal.add(Calendar.HOUR_OF_DAY, tz);
		int zuluCalHour = cal.get(Calendar.HOUR_OF_DAY);
		String msg = (
				"@"+String.format("%02d" , cal.get(Calendar.DAY_OF_MONTH))
				+String.format("%02d" , zuluCalHour)
				+String.format("%02d" , calMinute)
				+"z"+getLatLonAprs(latitude, longitude));
		
		return msg;
	}
	public String getLocMsg(double latitude, double longitude, String comment) {

		String msg = ("="+getLatLonAprs(latitude, longitude)+"&"+comment);
		
		return msg;
	}
	
}
