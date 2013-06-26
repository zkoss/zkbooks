package org.zkoss.zssessentials.bean.spring;

public class DataBeanImpl {
	private double liquidAssets;
	private double fundInvestment;
	private double fixedAssets;
	private double intangibleAsset; 
	private double otherAssets;
	private double currentLiabilities;
	private double longTermLiabilities;
	private double otherLiabilities;
	private double capitalStock;
	private double capitalSurplus;
	private double retainedEarnings;
	private double otherEquity;
	private double treasuryStock;
	public DataBeanImpl() {
		//initial value
        setLiquidAssets(146504221);
		setFundInvestment(23181709);
		setFixedAssets(7168392);
		setIntangibleAsset(221426);
		setOtherAssets(2270018);
		setCurrentLiabilities(102515784);
		setLongTermLiabilities(3000);
		setOtherLiabilities(456175);
		setCapitalStock(33630080);
		setCapitalSurplus(7127901);
		setRetainedEarnings(34420905);
		setOtherEquity(1826731);
		setTreasuryStock(-634810);
	}
	public double getLiquidAssets() {
		return liquidAssets;
	}
	public void setLiquidAssets(double liquidAssets) {
		this.liquidAssets = liquidAssets;
	}
	public double getFundInvestment() {
		return fundInvestment;
	}
	public void setFundInvestment(double fundInvestment) {
		this.fundInvestment = fundInvestment;
	}
	public double getFixedAssets() {
		return fixedAssets;
	}
	public void setFixedAssets(double fixedAssets) {
		this.fixedAssets = fixedAssets;
	}
	public double getIntangibleAsset() {
		return intangibleAsset;
	}
	public void setIntangibleAsset(double intangibleAsset) {
		this.intangibleAsset = intangibleAsset;
	}
	public double getOtherAssets() {
		return otherAssets;
	}
	public void setOtherAssets(double otherAssets) {
		this.otherAssets = otherAssets;
	}
	public double getCurrentLiabilities() {
		return currentLiabilities;
	}
	public void setCurrentLiabilities(double currentLiabilities) {
		this.currentLiabilities = currentLiabilities;
	}
	public double getLongTermLiabilities() {
		return longTermLiabilities;
	}
	public void setLongTermLiabilities(double longTermLiabilities) {
		this.longTermLiabilities = longTermLiabilities;
	}
	public double getOtherLiabilities() {
		return otherLiabilities;
	}
	public void setOtherLiabilities(double otherLiabilities) {
		this.otherLiabilities = otherLiabilities;
	}
	public double getCapitalStock() {
		return capitalStock;
	}
	public void setCapitalStock(double capitalStock) {
		this.capitalStock = capitalStock;
	}
	public double getCapitalSurplus() {
		return capitalSurplus;
	}
	public void setCapitalSurplus(double capitalSurplus) {
		this.capitalSurplus = capitalSurplus;
	}
	public double getRetainedEarnings() {
		return retainedEarnings;
	}
	public void setRetainedEarnings(double retainedEarnings) {
		this.retainedEarnings = retainedEarnings;
	}
	public double getOtherEquity() {
		return otherEquity;
	}
	public void setOtherEquity(double otherEquity) {
		this.otherEquity = otherEquity;
	}
	public double getTreasuryStock() {
		return treasuryStock;
	}
	public void setTreasuryStock(double treasuryStock) {
		this.treasuryStock = treasuryStock;
	}
}
