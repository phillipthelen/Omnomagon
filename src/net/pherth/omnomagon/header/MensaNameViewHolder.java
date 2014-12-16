package net.pherth.omnomagon.header;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;
import net.pherth.omnomagon.R;

public class MensaNameViewHolder {

    private final TextView _mensaName;

    public MensaNameViewHolder(@NonNull Activity activity) {
        final TextView mensaNameView = (TextView) activity.findViewById(R.id.menu_overview_header_mensa_name);
        final AssetManager assets = activity.getAssets();
        final Typeface regular = assets != null ? Typeface.createFromAsset(assets, "Roboto-Regular.ttf") : null;
        mensaNameView.setTypeface(regular);
        _mensaName = mensaNameView;
    }

    public void setMensaName(@NonNull String mensaName) {
        _mensaName.setText(mensaName);
    }
}
