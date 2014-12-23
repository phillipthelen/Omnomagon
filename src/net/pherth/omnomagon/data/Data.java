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

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import net.pherth.omnomagon.Util;
import net.pherth.omnomagon.data.mensa.DatabaseSnapshot;
import net.pherth.omnomagon.data.mensa.MensaBerlin;
import net.pherth.omnomagon.data.mensa.MensaUlm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Data {

	final private DatabaseProvider _databaseProvider;
	private Context _context;

	public Data(@NonNull Context context) {
		_databaseProvider = new DatabaseProvider(context);
		_context = context;
	}

	@Nullable
	public List<Day> parseData(@NonNull MensaBerlin mensaBerlin) {
		List<Day> data = null;
		final String mensaBerlinHTML = mensaBerlin.getHTML();
		if (mensaBerlinHTML != null) {
			data = parseHTMLBerlin(mensaBerlinHTML);
			saveDataInDatabase(data);
		}
		return data;
	}

	@Nullable
	public List<Day> parseData(@NonNull MensaUlm mensaUlm) {
		List<Day> data = null;
		final String mensaUlmXML = mensaUlm.getXML();
		if (mensaUlmXML != null) {
			data = parseXMLUlm(mensaUlmXML);
			saveDataInDatabase(data);
		}
		return data;
	}

	private void saveDataInDatabase(@Nullable List<Day> data) {
		try {
			if (data != null && !data.isEmpty()) {
				_databaseProvider.open();
				_databaseProvider.newData(data);
				_databaseProvider.close();
				DatabaseSnapshot.storeMetadata(_context);
			}
		} catch (SQLiteException e) {
            e.printStackTrace();
        }
	}

	@Nullable
	public List<Day> loadDataFromDatabase() {
		List<Day> data = null;
		try {
			_databaseProvider.open();
			data = _databaseProvider.getData();
			_databaseProvider.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Nullable
	private List<Day> parseHTMLBerlin(String htmlString) {
		final List<Day> data = new ArrayList<Day>(5);
		if (Util.getDebuggable(_context)) Log.i("HTML", "beginning to parse");
		Document doc = Jsoup.parse(htmlString);
		Elements headers = doc.getElementsByClass("mensa_week_head_col");
		for (int x = 0; x < headers.size(); x++) {
			String dateString = headers.get(x).ownText().substring(4);
			Date date = new Date();
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy");
				date = sdfToDate.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			data.add(new Day(date));
		}

		Elements rows = doc.getElementsByTag("tr");
		for (int x = 1; x < rows.size(); x++) {
			Element row = rows.get(x);
			String groupname = row.getElementsByClass("mensa_week_speise_tag_title").get(0).ownText();
			MealGroup mealGroup;
			try {
				mealGroup = MealGroup.valueOf(groupname);
			} catch (IllegalArgumentException ignore) {
				mealGroup = MealGroup.Essen;
			}
			Elements cols = row.getElementsByClass("mensa_week_speise_tag");
			for (int y = 0; y < cols.size(); y++) {
				List<Meal> meals = new ArrayList<Meal>();
				Day currday = data.get(y);
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
						Float[] priceFloatList = new Float[]{firstPrice, firstPrice, firstPrice};
						for (int number = 1; number < priceList.length; number++) {
							priceFloatList[number] = Float.parseFloat(priceList[number]);
						}
						meal.setPrices(priceFloatList);
					} else {
						meal.setPrices(new Float[]{(float) 0, (float) 0, (float) 0});
					}
					Elements additions = mealElement.getElementsByAttributeValue("href", "#zusatz");
					for (int i = 0; i < additions.size(); i++) {
						meal.addAddition(additions.get(i).attributes().get("title"));
					}

					Element prevElem = mealElement.previousElementSibling();
					if (prevElem != null && prevElem.tagName().equals("a")) {
						meal.setSiegel(prevElem.attributes().get("href"));
						Element prevprevElem = prevElem.previousElementSibling();
						if (prevprevElem != null && prevElem.tagName().equals("a")) {
							meal.setSiegel(prevprevElem.attributes().get("href"));
						}
					}
					meals.add(meal);

				}
				currday.addMealGroup(mealGroup, meals);
				data.set(y, currday);
			}
		}
		return data;
	}

	@Nullable
	private List<Day> parseXMLUlm(String xml) {
		final List<Day> data = new ArrayList<Day>(5);
		if(Util.getDebuggable(_context)) Log.i("HTML", "beginning to parse");
		Document doc = Jsoup.parse(xml);
		Elements weeks = doc.getElementsByTag("week");
		Element week = weeks.get(0);
		Elements days = week.getElementsByTag("day");
		for (final Element currentDay : days) {
			String dateString = currentDay.attr("date");
			Date date = new Date();
			try {
				SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
				date = sdfToDate.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			final List<Meal> meals = new ArrayList<Meal>();

			final Elements items = currentDay.getElementsByTag("item");
			for (Element item : items) {
				final Element parent = item.parent();
				boolean vegetarian = parent.hasAttr("type") && parent.attr("type").equalsIgnoreCase("Vegetarische Gerichte");
				final String text = item.text();
				final String name;
				final String additions;
				if (text != null && text.contains("mit")) {
					final String[] split = text.split(Pattern.quote("mit"), 2);
					if (split.length < 2) {
						name = text;
						additions = null;
					} else {
						name = split[0].trim();
						additions = split[1] != null ? split[1].trim() : "";
					}
				} else {
					name = text;
					additions = null;
				}
				if (name != null) {
					if (additions != null && additions.length() > 0) {
						meals.add(Meal.create(name, vegetarian, false, false, false, additions));
					} else {
						meals.add(Meal.create(name, vegetarian, false, false, false));
					}
				}
			}

			final Day day = new Day(date);
			day.addMealGroup(MealGroup.Essen, meals);
			data.add(day);
		}
		return data;
	}
}


