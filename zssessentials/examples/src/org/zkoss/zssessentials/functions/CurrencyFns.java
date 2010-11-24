/** CurrencyFns.java
 * 
 */
package org.zkoss.zssessentials.functions;


/**
 * @author ashish
 * 
 */
public class CurrencyFns {

	public static double toTWD(double usdNum, double twdRate) {
		return usdNum * twdRate;
	}

	public static double toTWD(double usdNum) {
		final double twdRate = 31.00d;
		return usdNum * twdRate;
	}
}
