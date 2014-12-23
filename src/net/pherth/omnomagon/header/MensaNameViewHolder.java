package net.pherth.omnomagon.header;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import net.pherth.omnomagon.R;

public class MensaNameViewHolder {

    private final ActionBarActivity _activity;
    private final TextView _mensaName;

    public MensaNameViewHolder(@NonNull ActionBarActivity activity) {
        final Configuration configuration = activity.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            _activity = activity;
            _mensaName = null;
        } else {
            _activity = null;
            final TextView mensaNameView = (TextView) activity.findViewById(R.id.menu_overview_header_mensa_name);
            final AssetManager assets = activity.getAssets();
            final Typeface regular = assets != null ? Typeface.createFromAsset(assets, "Roboto-Regular.ttf") : null;
            mensaNameView.setTypeface(regular);
            _mensaName = mensaNameView;
        }
    }

    public void setMensaName(@NonNull String mensaName) {
        if (_activity != null) {
            final ActionBar actionBar = _activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mensaName);
        } else {
            _mensaName.setText(mensaName);
        }
    }
}
