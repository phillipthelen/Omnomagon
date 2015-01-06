package net.pherth.omnomagon.header;

import android.support.annotation.NonNull;

public class FeatureImageDimensions extends BitmapDimensions {

    private final int _numberOfTabs;
    private final int _tabOffset;

    @NonNull
    public static FeatureImageDimensions of(int height, int width, int numberOfTabs, int tabOffset) {
        return new FeatureImageDimensions(height, width, numberOfTabs, tabOffset);
    }

    public FeatureImageDimensions(int height, int width, int numberOfTabs, int tabOffset) {
        super(height, width);
        _numberOfTabs = numberOfTabs;
        _tabOffset = tabOffset;
    }

    public int getOffsetForTabIndex(int index) {
        final int maxIndex = (_numberOfTabs - 1);
        final int cappedIndex = (index > maxIndex) ? maxIndex : index;
        return cappedIndex * _tabOffset;
    }
}
