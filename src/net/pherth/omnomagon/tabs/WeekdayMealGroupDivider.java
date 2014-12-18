package net.pherth.omnomagon.tabs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import net.pherth.omnomagon.R;

public class WeekdayMealGroupDivider extends RecyclerView.ItemDecoration {

    private final Drawable _divider;
    private final int _height;

    public WeekdayMealGroupDivider(@NonNull Context context) {
        final Resources resources = context.getResources();
        _divider = resources.getDrawable(R.drawable.divider_weekday_meal_group);
        _height = _divider.getIntrinsicHeight();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i=1; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final Object tag = child.getTag();
            if (tag != null && tag instanceof Boolean && ((Boolean) tag)) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = top + _height;
                _divider.setBounds(left, top, right, bottom);
                _divider.draw(c);
            }
        }
    }
}
