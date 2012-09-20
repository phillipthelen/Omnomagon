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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.sqlite.SQLiteException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

public class Data {
	public static final String TAG = Data.class.getSimpleName();
	List<Day> res;
	private Context context;
	SharedPreferences sharedPrefs;
	Dataprovider dataprov;
	
	public Data(Context cxt) {
		dataprov = new Dataprovider(cxt);
		this.context = cxt;
	}
	
	public void getAllData(boolean fromDatabase) {
		res = new ArrayList<Day>();
		if(fromDatabase) {
			if(Util.getDebuggable(context)) Log.i("Data", "Load from database");
			loadDataFromDatabase();
		} else {
			if(Util.getDebuggable(context)) Log.i("Data", "Load new data");
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
			String city = sharedPrefs.getString("cityPreference", "beList");
			String mensa = sharedPrefs.getString("mensaPreference", "fu1");
			String url = getURL(city, mensa);
			RSSHandler rh = new RSSHandler();
			String htmlString= rh.getHTML(url, city);
			if (htmlString != null) {
				parseString(htmlString, city);
			} else {
			}
			try {
				dataprov.open();
				dataprov.newData(res);
				dataprov.close();
			} catch (SQLiteException e) {
				e.printStackTrace();
			}


			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putLong("lastUpdate", System.currentTimeMillis());
			editor.commit();
		}
	}
	
	public void loadDataFromDatabase() {
		try {
			dataprov.open();
			res = dataprov.getData();
			dataprov.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
}
	
	public int getDayCount() {
		if (res == null) {
			return 0;
		}
		return res.size();
	}
	
	private void parseString(String html, String id) {
		if (id.equals("beList")) {
			parseHTMLBerlin(html);
		} else if (id.equals("ulm")) {
			parseXMLUlm(html);
		}
	}
	
	private void parseHTMLBerlin(String htmlString) {
		if(Util.getDebuggable(context)) Log.i("HTML", "beginning to parse");		
		Document doc = Jsoup.parse(htmlString);
		Elements headers = doc.getElementsByClass("mensa_week_head_col");
		for (int x=0; x < headers.size(); x++){
			String dateString = headers.get(x).ownText().substring(4);
			Date date1 = new Date();
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
				date1 = sdfToDate.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res.add(new Day(date1));
		}
		
		Elements rows = doc.getElementsByTag("tr");
		for (int x=1; x < rows.size(); x++) {
			Element row = rows.get(x);
			String groupname = row.getElementsByClass("mensa_week_speise_tag_title").get(0).ownText();
			int groupID;
			if (groupname.equals("Aktionsstand")) {
				groupID = R.drawable.ic_aktion;
			} else if (groupname.equals("Beilagen")) {
				groupID = R.drawable.ic_beilagen;
			} else if (groupname.equals("Desserts")) {
				groupID = R.drawable.ic_desserts;
			} else if (groupname.equals("Essen")) {
				groupID = R.drawable.ic_essen;
			} else if (groupname.equals("Salate")) {
				groupID = R.drawable.ic_salate;
			} else if (groupname.equals("Suppen")) {
				groupID = R.drawable.ic_suppen;
			} else if (groupname.equals("Vorspeisen")) {
				groupID = R.drawable.ic_vorspeisen;
			} else {
				groupID = R.drawable.ic_essen;
			}
			Elements cols = row.getElementsByClass("mensa_week_speise_tag");
			for (int y=0; y < cols.size(); y++) {
				List<Meal> meals = new ArrayList<Meal>();
				Day currday = res.get(y);
				Element col = cols.get(y);
				Elements currmeals = col.getElementsByClass("mensa_speise");
				for (int c = 0; c < currmeals.size(); c++) {
					Element mealElement = currmeals.get(c);
					Meal meal = new Meal(mealElement.getElementsByTag("strong").get(0).ownText() + " " + mealElement.ownText());
					String priceString = mealElement.getElementsByClass("mensa_preise").get(0).ownText();
					if (priceString.length() > 3) {
						priceString = priceString.substring(4);
						String[] priceList = priceString.split(" / ");
						Float firstPrice = Float.parseFloat(priceList[0]);
						Float[] priceFloatList = new Float[] {firstPrice, firstPrice, firstPrice};
						for (int number = 1; number < priceList.length; number++) {
							priceFloatList[number] = Float.parseFloat(priceList[number]);
						}
						meal.setPrices(priceFloatList);
					} else {
						meal.setPrices(new Float[]{(float) 0, (float) 0, (float) 0});
					}
					Elements additions = mealElement.getElementsByAttributeValue("href", "#zusatz");
					for (int i=0; i<additions.size(); i++) {
						meal.addAddition(additions.get(i).attributes().get("title"));
					}
					
					Element prevElem = mealElement.previousElementSibling();
					if (prevElem != null && prevElem.tagName() == "a") {
						meal.setSiegel(prevElem.attributes().get("href"));
						Element prevprevElem = prevElem.previousElementSibling();
						if (prevprevElem != null && prevElem.tagName() == "a" ) {
							meal.setSiegel(prevprevElem.attributes().get("href"));
						}
					}
					meals.add(meal);
					
				}
				currday.addMealGroup(groupID, groupname.toUpperCase(), meals);
				res.set(y, currday);
			}
		}
	}
	
	private void parseXMLUlm(String xml) {
		if(Util.getDebuggable(context)) Log.i("HTML", "beginning to parse");		
		Document doc = Jsoup.parse(xml);
		Elements weeks = doc.getElementsByTag("week");
		Element week = weeks.get(0);
		Elements days = week.getElementsByTag("day");
		for (int x=0; x < days.size(); x++){
			String dateString = days.get(x).ownText().substring(4);
			Date date1 = new Date();
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
				date1 = sdfToDate.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res.add(new Day(date1));
		}
		
	}
	

	public List<Pair<Pair<Integer, String>, List<Meal>>> getCurrentDay(int position) {
		List<Pair<Pair<Integer, String>, List<Meal>>> currentMeals = new ArrayList<Pair<Pair<Integer, String>, List<Meal>>>();
		Day currentDay = res.get(position);
		currentMeals = currentDay.getMeals();
		if(Util.getDebuggable(context)) Log.i("Current Meals", currentMeals.toString());
		return currentMeals;
	}
	
	private String getURL(String city, String mensa) {
		String url = "";
		if (city.equals("beList")) {
			url = "http://www.studentenwerk-berlin.de/speiseplan/rss/" + mensa + "/woche/lang/1";
		} else if (city.equals("ulm")) {
			url = "http://www.uni-ulm.de/mensaplan/mensaplan.xml";
		}
		if(Util.getDebuggable(context)) Log.i("url", city + ", " + mensa + ", " + url);
		return url;
	}
}


