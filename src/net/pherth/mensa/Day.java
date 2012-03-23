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

package net.pherth.mensa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;
import android.util.Pair;

public class Day {
	List<Pair<Integer, List<Meal>>> allMeals = new ArrayList<Pair<Integer, List<Meal>>>();
	public Date date;
	public String id;
	
	public Day(Date currentDay) {
		date = currentDay;
	}
	
	public Day(String currentDayString) {
		try {
			SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
			date = sdfToDate.parse(currentDayString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Day(String currentDayString, String dayid) {
		try {
			SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
			date = sdfToDate.parse(currentDayString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		id = dayid;
	}
	
	public List<Pair<Integer, List<Meal>>> getMeals() {
		return allMeals;
	}
	
	public void addMealGroup(Integer group) {
		allMeals.add(new Pair<Integer, List<Meal>>(group, new ArrayList<Meal>()));
	}
	
	public void addMealGroup(Integer group, List<Meal> meals) {
		allMeals.add(new Pair<Integer, List<Meal>>(group, meals));
	}
	
	public void addMealGroup(Pair<Integer, List<Meal>> meals) {
		allMeals.add(meals);
	}
	
	public void addMeal(Integer groupID, Meal meal) {
		boolean found = false;
		for(int group = 0; group < allMeals.size(); group++) {
			if (allMeals.get(group).first.equals(groupID)) {
				allMeals.get(group).second.add(meal);
				found = true;
			}
		}
		
		if (!found) {
			List<Meal> meallist = new ArrayList<Meal>();
			meallist.add(meal);
			allMeals.add(new Pair<Integer, List<Meal>>(groupID, meallist));
		}
	}
	
	public void setMeals(List<Pair<Integer, List<Meal>>> meals) {
		allMeals = meals;
	}
}
