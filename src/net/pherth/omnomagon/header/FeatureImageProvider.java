package net.pherth.omnomagon.header;

import android.support.annotation.DrawableRes;
import net.pherth.omnomagon.R;

import java.util.Random;

public class FeatureImageProvider {

    public static final int[] FEATURE_IMAGE_IDS = {
            R.drawable.feature_image_1,
            R.drawable.feature_image_2,
            R.drawable.feature_image_3,
            R.drawable.feature_image_4
    };

    private Random _random = new Random();

    @DrawableRes
    public int getRandomImageResource() {
        final int randomIndex = _random.nextInt(FEATURE_IMAGE_IDS.length);
        return FEATURE_IMAGE_IDS[randomIndex];
    }

    @DrawableRes
    public int getRandomImageResourceWithout(@DrawableRes int id) {
        final int[] reducedFeatureImages = new int[FEATURE_IMAGE_IDS.length - 1];
        int currentIndex = 0;
        for (int currentId : FEATURE_IMAGE_IDS) {
            if (currentId != id) {
                reducedFeatureImages[currentIndex] = currentId;
                currentIndex++;
            }
        }
        final int randomIndex = _random.nextInt(reducedFeatureImages.length);
        return reducedFeatureImages[randomIndex];
    }
}
