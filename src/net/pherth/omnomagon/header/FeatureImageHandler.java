package net.pherth.omnomagon.header;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import net.pherth.omnomagon.R;

public class FeatureImageHandler implements ViewPager.OnPageChangeListener {

    private final Activity _activity;
    private final FeatureImageProvider _featureImageProvider;
    private final HorizontalScrollView _horizontalScrollView;

    private FeatureImageDimensions _featureImageDimensions;

    @DrawableRes
    private Integer _currentImage;

    public FeatureImageHandler(@NonNull Activity activity) {
        _activity = activity;
        final FeatureImageProvider featureImageProvider = new FeatureImageProvider();
        final int randomImageResource = featureImageProvider.getRandomImageResource();
        final BitmapDimensions bitmapDimensions = findBitmapDimensionsForFeatureImage(activity, randomImageResource);
        final FeatureImageDimensions featureImageDimensions = findFeatureImageDimensions(activity, bitmapDimensions);
        scaleViewAndImage(activity, randomImageResource, featureImageDimensions);
        _currentImage = randomImageResource;
        _featureImageProvider = featureImageProvider;
        _featureImageDimensions = featureImageDimensions;
        _horizontalScrollView = (HorizontalScrollView) activity.findViewById(R.id.menu_overview_header_container);
    }

    private BitmapDimensions findBitmapDimensionsForFeatureImage(@NonNull Activity activity, @DrawableRes int imageResource) {
        final Resources resources = activity.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, imageResource, options);
        return BitmapDimensions.of(options.outHeight, options.outWidth);
    }

    private FeatureImageDimensions findFeatureImageDimensions(@NonNull Activity activity, @NonNull BitmapDimensions bitmapDimensions) {
        final int featureImageHeight = getFeatureImageHeight(activity);
        final int displayWidth = getDisplayWidth(activity);
        final FeatureImageScalingHelper scalingHelper = new FeatureImageScalingHelper(featureImageHeight, displayWidth, 7, 20);
        return scalingHelper.calculateDimensions(bitmapDimensions);
    }

    private int getFeatureImageHeight(@NonNull Activity activity) {
        final Resources resources = activity.getResources();
        return resources.getDimensionPixelSize(R.dimen.menu_overview_header_layout_height);
    }

    private int getDisplayWidth(@NonNull Activity activity) {
        final WindowManager windowManager = activity.getWindowManager();
        final Display defaultDisplay = windowManager.getDefaultDisplay();
        final int width;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            width = getDisplayWidth(defaultDisplay);
        } else {
            width = getDisplayWidthFallback(defaultDisplay);
        }
        return width;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private int getDisplayWidth(@NonNull Display display) {
        final Point outSize = new Point();
        display.getSize(outSize);
        return outSize.x;
    }

    @SuppressWarnings("deprecation")
    private int getDisplayWidthFallback(@NonNull Display display) {
        return display.getWidth();
    }

    private void scaleViewAndImage(@NonNull Activity activity, @DrawableRes int imageResource, @NonNull FeatureImageDimensions featureImageDimensions) {
        final int width = featureImageDimensions.getWidth();
        final int height = featureImageDimensions.getHeight();
        final ImageView featureImage = scaleView(activity, width);
        final Bitmap scaledBitmap = getScaledBitmap(activity, imageResource, width, height);
        featureImage.setImageBitmap(scaledBitmap);
    }

    @NonNull
    private ImageView scaleView(@NonNull Activity activity, int width) {
        final ImageView featureImage = (ImageView) activity.findViewById(R.id.menu_overview_header_image);
        final ViewGroup.LayoutParams layoutParams = featureImage.getLayoutParams();
        layoutParams.width = width;
        featureImage.setLayoutParams(layoutParams);
        return featureImage;
    }

    @NonNull
    private Bitmap getScaledBitmap(@NonNull Activity activity, @DrawableRes int imageResource, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = findNearestSampleSize(options.outWidth, width);
        options.inJustDecodeBounds = false;
        final Resources resources = activity.getResources();
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, imageResource, options);
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (bitmap != scaledBitmap) {
            bitmap.recycle();
        }
        return scaledBitmap;
    }

    private int findNearestSampleSize(int measuredWidth, int targetWidth) {
        int sampleSize = 1;
        while ((measuredWidth/(sampleSize*2)) >= targetWidth) {
            sampleSize *= 2;
        }
        return sampleSize;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        final int featureImageOffset = _featureImageDimensions.getOffsetForTabIndex(position);
        _horizontalScrollView.smoothScrollTo(featureImageOffset, 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    public void switchFeatureImage() {
        final int randomImageResource;
        if (_currentImage != null) {
            randomImageResource = _featureImageProvider.getRandomImageResourceWithout(_currentImage);
        } else {
            randomImageResource = _featureImageProvider.getRandomImageResource();
        }
        final BitmapDimensions bitmapDimensions = findBitmapDimensionsForFeatureImage(_activity, randomImageResource);
        final FeatureImageDimensions featureImageDimensions = findFeatureImageDimensions(_activity, bitmapDimensions);
        scaleViewAndImage(_activity, randomImageResource, featureImageDimensions);
        _currentImage = randomImageResource;
        _featureImageDimensions = featureImageDimensions;
    }
}
