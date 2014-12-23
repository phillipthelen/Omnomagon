package net.pherth.omnomagon.header;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import net.pherth.omnomagon.R;

public class RefreshAnimationHelper {

    private View _refreshButton;
    private RotateAnimation _rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public RefreshAnimationHelper(@NonNull Activity activity) {
        _refreshButton = activity.findViewById(R.id.actionbar_refresh);
        _rotateAnimation.setDuration(2000L);
        _rotateAnimation.setRepeatMode(Animation.RESTART);
    }

    public void refreshViewHint(@NonNull Activity activity) {
        _refreshButton = activity.findViewById(R.id.actionbar_refresh);
    }

    public boolean canAnimate() {
        return _refreshButton != null;
    }

    public void startAnimation() {
        _rotateAnimation.setRepeatCount(Animation.INFINITE);
        if (canAnimate()) {
            _refreshButton.startAnimation(_rotateAnimation);
        }
    }

    public void endAnimation() {
        _rotateAnimation.setRepeatCount(0);
    }
}
