package org.springframework.webflow.samples.booking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class ShortDate implements TypeConverter {

	public Object coerceToBean(Object val, Component comp) {
		return val;
	}

	public Object coerceToUi(Object val, Component comp) {
		if (val == null) return null;
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locales.getCurrent());
		final TimeZone tz = TimeZone.getTimeZone("EST");
		df.setTimeZone(tz);
		return df.format((Date)val);
	}

}
