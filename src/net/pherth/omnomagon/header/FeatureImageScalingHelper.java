package net.pherth.omnomagon.header;

import android.support.annotation.NonNull;

public class FeatureImageScalingHelper {

    private final int _targetHeight;
    private final int _targetWidth;
    private final int _numberOfTabs;
    private final int _minimalTabOffset;

    public FeatureImageScalingHelper(int targetHeight, int targetWidth, int numberOfTabs, int minimalTabOffset) {
        _targetHeight = targetHeight;
        _targetWidth = targetWidth;
        _numberOfTabs = numberOfTabs;
        _minimalTabOffset = minimalTabOffset;
    }

    @NonNull
    public FeatureImageDimensions calculateDimensions(@NonNull BitmapDimensions originalDimensions) {
        final FeatureImageDimensions featureImageDimensions;
        final float ratio = originalDimensions.getRatio();
        final int calculatedHeight = calculateHeight(originalDimensions);
        final int calculatedWidth = calculateWidthFromHeight(calculatedHeight, ratio);
        final int furtherWidthNeeded = furtherWidthNeeded(calculatedWidth);
        if (furtherWidthNeeded > 0) {
            final int newTargetWidth = calculatedWidth + furtherWidthNeeded;
            final int newTargetHeight = calculateHeightFromWidth(newTargetWidth, ratio);
            featureImageDimensions = FeatureImageDimensions.of(newTargetHeight, newTargetWidth, _numberOfTabs, _minimalTabOffset);
        } else {
            final int tabOffset = calculateTabOffset(calculatedWidth);
            featureImageDimensions = FeatureImageDimensions.of(calculatedHeight, calculatedWidth, _numberOfTabs, tabOffset);
        }
        return featureImageDimensions;
    }

    private int calculateHeight(@NonNull BitmapDimensions originalDimensions) {
        final int height = originalDimensions.getHeight();
        return height >= _targetHeight ? height : _targetHeight;
    }

    private int calculateWidthFromHeight(int height, float ratio) {
        final Float width = ratio != 0 ? height / ratio : 0;
        return width.intValue();
    }

    private int furtherWidthNeeded(int width) {
        final int minimalOffsetNeeded = (_numberOfTabs -1) * _minimalTabOffset;
        final int minimalWidth = minimalOffsetNeeded + _targetWidth;
        return (width >= minimalWidth) ? 0 : Math.abs(minimalWidth - width);
    }

    private int calculateHeightFromWidth(int width, float ratio) {
        final Float height = width * ratio;
        return height.intValue();
    }

    private int calculateTabOffset(int width) {
        final float offsetToDistribute = Math.abs(width - _targetWidth);
        final Float tabOffset = (_numberOfTabs -1) > 0 ? offsetToDistribute / (_numberOfTabs -1) : 0;
        return tabOffset.intValue();
    }
}
