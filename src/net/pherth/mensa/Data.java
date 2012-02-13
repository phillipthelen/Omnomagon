package net.pherth.mensa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

public class Data {
	public static final String TAG = Data.class.getSimpleName();
	List<Day> res;
	private Context context;
	
	public Data(Context cxt) {
		this.context = cxt;
	}
	
	public void getAllData() {
		res = new ArrayList<Day>();
		
		String url = getURL();
		
		RSSHandler rh = new RSSHandler();
		String htmlString= rh.getHTML(url);
		parseHTML(htmlString);
	}
	
	
	public int getDayCount() {
		if (res == null) {
			return 0;
		}
		return res.size();
	}
	
	private void parseHTML(String htmlString) {
		System.out.println("parsing HTML");		
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
			Elements cols = row.getElementsByClass("mensa_week_speise_tag");
			for (int y=0; y < cols.size(); y++) {
				List<Meal> meals = new ArrayList<Meal>();
				Day currday = res.get(y);
				Element col = cols.get(y);
				Elements currmeals = col.getElementsByClass("mensa_speise");
				for (int c = 0; c < currmeals.size(); c++) {
					Element mealElement = currmeals.get(c);
					Meal meal = new Meal(mealElement.getElementsByTag("strong").get(0).ownText());
					meal.setDescription(mealElement.ownText());
					String priceString = mealElement.getElementsByClass("mensa_preise").get(0).ownText().substring(4);
					String[] priceList = priceString.split(" / ");
					Float firstPrice = Float.parseFloat(priceList[0]);
					Float[] priceFloatList = new Float[] {firstPrice, firstPrice, firstPrice};
					for (int number = 1; number < priceList.length; number++) {
						priceFloatList[number] = Float.parseFloat(priceList[number]);
					}
					meal.setPrices(priceFloatList);
					
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
				currday.addMealGroup(groupname, meals);
				res.set(y, currday);
			}
		}
	}
	

	public List<Pair<String, List<Meal>>> getCurrentDay(int position) {
		List<Pair<String, List<Meal>>> currentMeals = new ArrayList<Pair<String, List<Meal>>>();
		Day currentDay = res.get(position);
		currentMeals = currentDay.getMeals();
		System.out.println(currentMeals);
		return currentMeals;
	}
	
	private String getURL() {
		String url;
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		String mensa = sharedPrefs.getString("mensaPreference", "fu1");
		url = "http://www.studentenwerk-berlin.de/speiseplan/rss/" + mensa + "/woche/lang/1";
		return url;
	}
}


