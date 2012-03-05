package org.springframework.webflow.samples.booking;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class BoolToInt implements TypeConverter {

	public Object coerceToBean(Object val, Component comp) {
		return ((Integer)val).intValue() == 0;
	}

	public Object coerceToUi(Object val, Component comp) {
		return ((val != null && ((Boolean)val).booleanValue()) ? new Integer(0) : new Integer(1));
	}

}
