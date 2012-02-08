package net.pherth.mensa;

import java.text.DecimalFormat;

public class Meal {
    
    private String name;
    private String description;
    private Float[] prices = new Float[] {(float) 0.1, (float) 0.2, (float) 0.3};
    
    public Meal(String name, String description) {
		this.name = name;
		this.description = description;
	}
    
    public Meal(String name) {
    	this.name = name;
    }
    
    public Meal() {
	}
    
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPrices(Float[] prices) {
    	this.prices = prices;
    }
    
    public Float[] getPrices(){
    	return this.prices;
    }
    
    public float getCorrectPrice(int type) {
    	return this.prices[type];
    }
    
    public String getCorrectPriceString(int type) {
    	DecimalFormat dec = new DecimalFormat();
    	dec.setMinimumFractionDigits(2);
    	return dec.format(this.prices[type]) + " Û";
    }
}