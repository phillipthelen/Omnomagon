package net.pherth.omnomagon.settings;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.SettingsActivity;

public class PriceSelectionViewHolder implements PopupMenu.OnMenuItemClickListener {

    private final UserPreferences _userPreferences;
    private final View _priceSelectionButton;
    private final TextView _priceSelectionValue;

    public PriceSelectionViewHolder(@NonNull SettingsActivity settingsActivity) {
        _userPreferences = settingsActivity.getUserPreferences();
        _priceSelectionButton = settingsActivity.findViewById(R.id.settings_price_button);
        _priceSelectionValue = (TextView) settingsActivity.findViewById(R.id.settings_price_value);
        initializeValues();
        PopupMenuHelper.configurePopupMenu(_priceSelectionButton, R.array.priceClasses, this);
    }

    private void initializeValues() {
        final Integer selectedPriceId = _userPreferences.getSelectedPriceId();
        if (selectedPriceId != null) {
            _priceSelectionValue.setText(selectedPriceId);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        final CharSequence title = menuItem.getTitle();
        _priceSelectionValue.setText(title);
        final int priceId = menuItem.getItemId();
        _userPreferences.setSelectedPriceId(priceId);
        return true;
    }
}
