/* DataBean.java

	Purpose:
		
	Description:
		
	History:
		Nov 23, 2010 4:27:15 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.bean.spring;

/**
 * DataBean carrying important values for a balance sheet. 
 * @author henrichen
 *
 */
public interface DataBean {
	public double getLiquidAssets();
	public void setLiquidAssets(double liquidAssets);
	
	public double getFundInvestment();
	public void setFundInvestment(double fundInvestment);
	
	public double getFixedAssets();
	public void setFixedAssets(double fixedAssets);
	
	public double getIntangibleAsset();
	public void setIntangibleAsset(double intangibleAsset);
	
	public double getOtherAssets();
	public void setOtherAssets(double otherAssets);
	
	public double getCurrentLiabilities();
	public void setCurrentLiabilities(double currentLiabilities);
	
	public double getLongTermLiabilities();
	public void setLongTermLiabilities(double longTermLiabilities);
	
	public double getOtherLiabilities();
	public void setOtherLiabilities(double otherLiabilities);
	
	public double getCapitalStock();
	public void setCapitalStock(double capitalStock);
	
	public double getCapitalSurplus();
	public void setCapitalSurplus(double capitalSurplus);
	
	public double getRetainedEarnings();
	public void setRetainedEarnings(double retainedEarnings);
	
	public double getOtherEquity();
	public void setOtherEquity(double otherEquity);
	
	public double getTreasuryStock();
	public void setTreasuryStock(double treasuryStock);
}
