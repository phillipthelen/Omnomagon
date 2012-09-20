package net.pherth.omnomagon;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.pm.PackageManager.NameNotFoundException;
public class AboutActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		String version = "";

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
		actionBar.setDisplayShowHomeEnabled(false);

		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("tag", e.getMessage());
		}

		TextView versionView = (TextView) findViewById(R.id.version);
		versionView.setText(version);

		TextView codeView = (TextView) findViewById(R.id.code);
		codeView.setText("Phillip Thelen");

		TextView graphicView = (TextView) findViewById(R.id.graphic);
		graphicView.setText("Peter Amende");

		TextView websiteView = (TextView) findViewById(R.id.website);
		websiteView.setText("omnomagon.de");

		TextView githubView = (TextView) findViewById(R.id.github);
		githubView.setText("github.com/viirus/omnomagon");

		TextView contactView = (TextView) findViewById(R.id.contact);
		contactView.setText("contact@omnomagon.de");


	}
}
