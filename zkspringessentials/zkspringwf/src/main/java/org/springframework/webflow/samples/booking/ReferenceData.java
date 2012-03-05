package org.springframework.webflow.samples.booking;

public class ReferenceData {
    public String[] getBedOptions() {
    	return new String[] {
    		"One king-size bed", 
    		"Two double beds", 
    		"Three beds"};
	}
    
    public String[] getCreditCardExpMonths() {
    	return new String[] {
    			"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
    			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	}

    public String[] getCreditCardExpYears() {
    	return new String[] {"2008", "2009", "2010", "2011", "2012"};
    }

    public String[] getPageSizeOptions() {
    	return new String[] {"5", "10", "20"};
    }

}
