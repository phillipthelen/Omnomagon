/*
Copyright (c) 2012, Phillip Thelen
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.pherth.omnomagon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Meal {
    
    private String name;
    private Float[] prices = new Float[] {(float) 0.1, (float) 0.2, (float) 0.3};
    private Boolean vegetarianBool = false;
    private Boolean veganBool = false;
    private Boolean bioBool = false;
    private Boolean mscBool = false;
    private List<String> additions = new ArrayList<String>();
    
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
    
    public void setPrices(Float[] prices) {
    	this.prices = prices;
    }
    
    public Float[] getPrices(){
    	return this.prices;
    }
    
    public void setVegetarian(Boolean vegetarianBool) {
    	this.vegetarianBool = vegetarianBool;
    }
    
    public Boolean getVegetarian() {
    	return this.vegetarianBool;
    }
    
    public void setVegan(Boolean veganBool) {
    	this.veganBool = veganBool;
    }
    
    public Boolean getVegan() {
    	return this.veganBool;
    }
    
    public void setBio(Boolean bioBool) {
    	this.bioBool = bioBool;
    }
    
    public Boolean getBio() {
    	return this.bioBool;
    }
    
    public void setMsc(Boolean mscBool) {
    	this.mscBool = mscBool;
    }
    
    public Boolean getMsc() {
    	return this.mscBool;
    }
    
    public int getVeganterianMsc(Context context) {
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    	if (sharedPrefs.getBoolean("vegetarianCheckbox", false) && this.vegetarianBool) {
    		return R.drawable.vegetarischsiegel;
    	} else if (sharedPrefs.getBoolean("vegetarianCheckbox", false) &&this.veganBool) {
    		return R.drawable.vegansiegel;
    	} else if (sharedPrefs.getBoolean("vegetarianCheckbox", false) &&this.mscBool) {
    		return 0;
    	}
    	
    	return 0;
    }
    
    public void addAddition(String addition) {
    	this.additions.add(addition);
    }
    
    public List<String> getAdditions() {
    	Log.i("Additions", this.additions.toString());
    	return this.additions;
    }
    
    public float getCorrectPrice(int type) {
    	return this.prices[type];
    }
    
    public void setSiegel(String siegellink) {
    	if (siegellink.equals("#vegan_siegel")) {
    		this.veganBool = true;
    	} else if (siegellink.equals("#vegetarisch_siegel")) {
    		this.vegetarianBool = true;
    	} else if (siegellink.equals("#bio_siegel")){
    		this.bioBool = true;
    	}
    }
    
    public String getCorrectPriceString(int type) {
    	DecimalFormat dec = new DecimalFormat();
    	dec.setMinimumFractionDigits(2);
    	return dec.format(this.prices[type]) + " €";
    }
    
    
}