package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import net.pherth.omnomagon.R;
import net.pherth.omnomagon.data.MealGroup;

public class WeekdayMealGroupViewHolder extends RecyclerView.ViewHolder {

    private final Context _context;
    private final ImageView _mealGroupIcon;
    private final TextView _mealGroupHeadline;

    public WeekdayMealGroupViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setTag(true);
        _context = itemView.getContext();
        _mealGroupIcon = (ImageView) itemView.findViewById(R.id.menu_overview_list_header_icon);
        _mealGroupHeadline = (TextView) itemView.findViewById(R.id.menu_overview_list_header_text);
    }

    public void setMealGroup(@NonNull MealGroup mealGroup) {
        _mealGroupIcon.setImageResource(MealGroup.getImageResourceFor(mealGroup));
        final String headline = _context.getString(MealGroup.getTextResourceFor(mealGroup));
        _mealGroupHeadline.setText(headline);
    }
}
