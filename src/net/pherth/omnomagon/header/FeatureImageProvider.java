package net.pherth.omnomagon.header;

import android.support.annotation.DrawableRes;
import net.pherth.omnomagon.R;

import java.util.Random;

public class FeatureImageProvider {

    public static final int[] FEATURE_IMAGE_IDS = {
            R.drawable.feature_image_4
    };

    private Random _random = new Random();

    @DrawableRes
    public int getRandomImageResource() {
        final int resourceId;
        if (FEATURE_IMAGE_IDS.length > 1) {
            final int randomIndex = _random.nextInt(FEATURE_IMAGE_IDS.length);
            resourceId = FEATURE_IMAGE_IDS[randomIndex];
        } else {
            resourceId = FEATURE_IMAGE_IDS[0];
        }
        return resourceId;
    }

    @DrawableRes
    public int getRandomImageResourceWithout(@DrawableRes int id) {
        final int resourceId;
        if (FEATURE_IMAGE_IDS.length > 1) {
            final int[] reducedFeatureImages = new int[FEATURE_IMAGE_IDS.length - 1];
            int currentIndex = 0;
            for (int currentId : FEATURE_IMAGE_IDS) {
                if (currentId != id) {
                    reducedFeatureImages[currentIndex] = currentId;
                    currentIndex++;
                }
            }
            final int randomIndex = _random.nextInt(reducedFeatureImages.length);
            resourceId = reducedFeatureImages[randomIndex];
        } else {
            resourceId = FEATURE_IMAGE_IDS[0];
        }
        return resourceId;
    }
}
