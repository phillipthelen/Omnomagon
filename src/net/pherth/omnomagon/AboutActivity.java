package net.pherth.omnomagon;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import net.pherth.omnomagon.about.Version;

public class AboutActivity extends ActionBarActivity {

    private View _versionButton;
    private TextView _versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        _versionButton = findViewById(R.id.about_version_button);
        _versionTextView = (TextView) findViewById(R.id.about_version_value);
        configureActionBar();
        configureVersion();
    }

    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureVersion() {
        String versionName;
        try {
            final PackageManager packageManager = getPackageManager();
            final String packageName = getPackageName();
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignore) {
            versionName = getString(R.string.about_version_failure);
        }
        _versionTextView.setText(versionName);
        _versionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Intent aboutVersionIntent = new Intent(AboutActivity.this, Version.class);
                startActivity(aboutVersionIntent);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            handled = true;
            finish();
        }
        return handled || super.onOptionsItemSelected(item);
    }

    public void sendMail(View view) {
        final String address = getString(R.string.about_contact_mail_value);
        final Uri mailto = Uri.fromParts("mailto", address, null);
        final Intent intent = new Intent(Intent.ACTION_SENDTO, mailto);
        final Intent chooser = Intent.createChooser(intent, null);
        startActivity(chooser);
    }

    public void goToGithub(View view) {
        final String address = getString(R.string.about_github_value);
        openBrowser(address);
    }

    private void openBrowser(@NonNull String address) {
        final Uri parsedUri = Uri.parse(address);
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(parsedUri);
        startActivity(intent);
    }

    public void goToWebsite(View view) {
        final String address = getString(R.string.about_website_value);
        openBrowser(address);
    }
}
