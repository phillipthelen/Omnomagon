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

package net.pherth.omnomagon.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Meal {
    
    private String name;
    private Float[] prices;
    private Boolean vegetarianBool = false;
    private Boolean veganBool = false;
    private Boolean bioBool = false;
    private Boolean mscBool = false;
    private List<String> additions = new ArrayList<String>();

    @NonNull
    public static Meal create(@NonNull String name, boolean vegetarianBool, boolean veganBool, boolean bioBool, boolean mscBool, @Nullable String... additions) {
        final Meal meal = new Meal(name);
        meal.setVegetarian(vegetarianBool);
        meal.setVegan(veganBool);
        meal.setBio(bioBool);
        meal.setMsc(mscBool);
        if (additions != null && additions.length > 0) {
            for (final String addition : additions) {
                meal.addAddition(addition);
            }
        }
        return meal;
    }

    @NonNull
    public static Meal create(@NonNull String name, float price1, float price2, float price3, boolean vegetarianBool, boolean veganBool, boolean bioBool, boolean mscBool, @Nullable String... additions) {
        final Meal meal = new Meal(name);
        meal.setPrices(new Float[] {price1, price2, price3});
        meal.setVegetarian(vegetarianBool);
        meal.setVegan(veganBool);
        meal.setBio(bioBool);
        meal.setMsc(mscBool);
        if (additions != null && additions.length > 0) {
            for (final String addition : additions) {
                meal.addAddition(addition);
            }
        }
        return meal;
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
    
    public void addAddition(String addition) {
    	this.additions.add(addition);
    }
    
    public List<String> getAdditions() {
    	Log.i("Additions", this.additions.toString());
    	return this.additions;
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

    public boolean hasPriceSet() {
        return prices != null;
    }
    
    public String getCorrectPriceString(int type) {
    	DecimalFormat dec = new DecimalFormat();
    	dec.setMinimumFractionDigits(2);
        final Float price = this.prices != null ? this.prices[type] : 0.0f;
        return dec.format(price) + " €";
    }
    
    
}