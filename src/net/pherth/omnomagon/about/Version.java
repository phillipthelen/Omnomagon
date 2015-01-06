package net.pherth.omnomagon.about;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import net.pherth.omnomagon.R;

import java.util.ArrayList;
import java.util.Random;

public class Version extends ActionBarActivity implements VersionListener, View.OnClickListener {

    private final AlphaAnimation _alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
    private final Random _random = new Random();
    private TextView _infoText;
    private View _top;
    private View _left;
    private View _right;
    private View _bottom;
    private ArrayList<Integer> _plan;
    private int _currentRound = 0;
    private int _playerIndex = 0;
    private boolean _buttonLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_version);
        _infoText = (TextView) findViewById(R.id.about_version_info);
        _top = findViewById(R.id.about_version_button_top);
        _top.setTag(0);
        _left = findViewById(R.id.about_version_button_left);
        _left.setTag(1);
        _right = findViewById(R.id.about_version_button_right);
        _right.setTag(2);
        _bottom = findViewById(R.id.about_version_button_bottom);
        _bottom.setTag(3);
        _alphaAnimation.setRepeatCount(1);
        _alphaAnimation.setRepeatMode(Animation.REVERSE);
        _alphaAnimation.setDuration(250L);
        configureActionBar();
        restart();
    }

    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void restart() {
        generatePlan();
        _currentRound = 0;
        setCountdownText("Watch and memorize! \nStart in %d", 5, 0L, this, "startRound");
    }

    private void generatePlan() {
        _plan = new ArrayList<Integer>(20);
        for (int i = 0; i < 20; i++) {
            final int anInt = _random.nextInt(4);
            _plan.add(anInt);
        }
    }

    @Override
    public void onUpdate(@NonNull String tag) {
        if ("startRoundWithCountdown".equals(tag)) {
            setCountdownText("Watch and memorize! \nStart in %d", 2, 0L, this, "startRound");
        } else if ("startRound".equals(tag)) {
            deactivateButtons();
            _infoText.setText("Watch and memorize!");
            animatePlanUpTo(0, _currentRound, 600);
        } else if ("startRepeat".equals(tag)) {
            _infoText.setText("Now Repeat");
            _playerIndex = 0;
            activateButtons();
        } else if ("win".equals(tag)) {
            deactivateButtons();
            final Resources resources = getResources();
            final int green = resources.getColor(R.color.mantis);
            _infoText.setTextColor(green);
            _infoText.setText("You Win :)");
        } else if ("loose".equals(tag)) {
            deactivateButtons();
            final Resources resources = getResources();
            final int orange = resources.getColor(R.color.crayon_orange);
            _infoText.setTextColor(orange);
            _infoText.setText("You loose :(");
        }
    }

    private void activateButtons() {
        _top.setOnClickListener(this);
        _left.setOnClickListener(this);
        _right.setOnClickListener(this);
        _bottom.setOnClickListener(this);
    }

    private void deactivateButtons() {
        _top.setOnClickListener(null);
        _left.setOnClickListener(null);
        _right.setOnClickListener(null);
        _bottom.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        if (!_buttonLock) {
            _buttonLock = true;
            v.startAnimation(_alphaAnimation);
            _alphaAnimation.setAnimationListener(generateAnimationListener());
            final boolean correctButton = didHitCorrectButton(v, _playerIndex);
            if (correctButton) {
                if (_playerIndex >= _currentRound) {
                    _currentRound++;
                    if (_currentRound < 20) {
                        onUpdate("startRoundWithCountdown");
                    } else {
                        onUpdate("win");
                    }
                } else {
                    _playerIndex++;
                }
            } else {
                onUpdate("loose");
            }
        }
    }

    @NonNull
    private Animation.AnimationListener generateAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                _buttonLock = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        };
    }

    private boolean didHitCorrectButton(View view, int index) {
        final Integer buttonId = (Integer) view.getTag();
        final Integer wantedButtonId = _plan.get(index);
        return wantedButtonId.equals(buttonId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            handled = true;
            finish();
        }
        return handled || super.onOptionsItemSelected(item);
    }

    private void setCountdownText(@NonNull final String text, final int seconds, long delay, @NonNull final VersionListener versionListener, @NonNull final String tag) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _infoText.setText(String.format(text, seconds));
                        }
                    });
                    if (seconds > 1) {
                        setCountdownText(text, seconds - 1, 1000L, versionListener, tag);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                versionListener.onUpdate(tag);
                            }
                        });
                    }
                }
            }
        }, delay);
    }

    private void animatePlanUpTo(final int start, final int end, final long delay) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final View view = getViewFor(start);
                            view.startAnimation(_alphaAnimation);
                        }
                    });
                }
                final int nextItem = start + 1;
                if (nextItem <= end) {
                    animatePlanUpTo(nextItem, end, delay);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onUpdate("startRepeat");
                        }
                    });
                }
            }
        }, delay);
    }

    @NonNull
    private View getViewFor(int index) {
        final View view;
        final Integer viewSelector = _plan.get(index);
        switch (viewSelector) {
            case 0:
                view = _top;
                break;
            case 1:
                view = _left;
                break;
            case 2:
                view = _right;
                break;
            default:
                view = _bottom;
                break;
        }
        return view;
    }
}
