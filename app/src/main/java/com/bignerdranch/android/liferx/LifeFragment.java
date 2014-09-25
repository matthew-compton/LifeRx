package com.bignerdranch.android.liferx;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class LifeFragment extends Fragment {

    @InjectView(R.id.button1) ToggleButton tileToggleButton1;
    @InjectView(R.id.button2) ToggleButton tileToggleButton2;
    @InjectView(R.id.button3) ToggleButton tileToggleButton3;
    @InjectView(R.id.button4) ToggleButton tileToggleButton4;
    @InjectView(R.id.button5) ToggleButton tileToggleButton5;
    @InjectView(R.id.button6) ToggleButton tileToggleButton6;
    @InjectView(R.id.button7) ToggleButton tileToggleButton7;
    @InjectView(R.id.button8) ToggleButton tileToggleButton8;
    @InjectView(R.id.button9) ToggleButton tileToggleButton9;

    @InjectView(R.id.restart) Button buttonRestart;
    @InjectView(R.id.timer) TextView mTimerTextView;
    @InjectView(R.id.counter) TextView mCounterTextView;

    private List<ToggleButton> tileButtons;
    private boolean mIsStarted;

    private Subscription mSubscription;
    private PublishSubject<ToggleButton> mToggleButtonPublishSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_life, container, false);
        ButterKnife.inject(this, layout);

        tileButtons = new ArrayList<ToggleButton>() {{
            add(tileToggleButton1);
            add(tileToggleButton2);
            add(tileToggleButton3);
            add(tileToggleButton4);
            add(tileToggleButton5);
            add(tileToggleButton6);
            add(tileToggleButton7);
            add(tileToggleButton8);
            add(tileToggleButton9);
        }};

        mIsStarted = false;

        mToggleButtonPublishSubject = PublishSubject.create();
        mSubscription = mToggleButtonPublishSubject.asObservable().subscribe(
                new Action1<ToggleButton>() {
                    @Override
                    public void call(ToggleButton button) {
                        incrementCounter();
                        if (!mIsStarted) {
                            mIsStarted = true;
                            mCountDownTimer.start();
                        }
                        toggleAdjacentToggleButtons(button);
                        if (checkForVictory()) {
                            isVictory();
                        }
                    }
                }
        );

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9})
    public void activate(ToggleButton toggleButton) {
        mToggleButtonPublishSubject.onNext(toggleButton);
    }

    @OnClick(R.id.restart)
    public void restart() {
        mCountDownTimer.cancel();
        mIsStarted = false;
        mCounterTextView.setText(getString(R.string.zero));
        mTimerTextView.setText(getString(R.string.time_max));
        mTimerTextView.setTextColor(getResources().getColor(android.R.color.white));
        resetToggleButtons();
    }

    private boolean checkForVictory() {
        for (ToggleButton toggleButton : tileButtons) {
            if (!toggleButton.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private void isVictory() {
        mCountDownTimer.cancel();
        disableToggleButtons();
        mTimerTextView.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        Toast.makeText(getActivity(), getString(R.string.victory), Toast.LENGTH_SHORT).show();
    }

    private void isDefeat() {
        disableToggleButtons();
        mTimerTextView.setText(getString(R.string.zero));
        mTimerTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        Toast.makeText(getActivity(), getString(R.string.defeat), Toast.LENGTH_SHORT).show();
    }

    private void disableToggleButtons() {
        for (ToggleButton toggleButton : tileButtons) {
            toggleButton.setEnabled(false);
        }
    }

    private void resetToggleButtons() {
        for (ToggleButton toggleButton : tileButtons) {
            toggleButton.setEnabled(true);
            toggleButton.setChecked(false);
        }
    }

    private void incrementCounter() {
        Integer counter = Integer.parseInt(mCounterTextView.getText().toString()) + 1;
        mCounterTextView.setText(counter.toString());
    }

    private void toggleAdjacentToggleButtons(ToggleButton toggleButton) {
        switch (toggleButton.getId()) {
            case R.id.button1:
                tileToggleButton2.toggle();
                tileToggleButton4.toggle();
                break;
            case R.id.button2:
                tileToggleButton1.toggle();
                tileToggleButton3.toggle();
                tileToggleButton5.toggle();
                break;
            case R.id.button3:
                tileToggleButton2.toggle();
                tileToggleButton6.toggle();
                break;
            case R.id.button4:
                tileToggleButton1.toggle();
                tileToggleButton5.toggle();
                tileToggleButton7.toggle();
                break;
            case R.id.button5:
                tileToggleButton2.toggle();
                tileToggleButton4.toggle();
                tileToggleButton6.toggle();
                tileToggleButton8.toggle();
                break;
            case R.id.button6:
                tileToggleButton3.toggle();
                tileToggleButton5.toggle();
                tileToggleButton9.toggle();
                break;
            case R.id.button7:
                tileToggleButton4.toggle();
                tileToggleButton8.toggle();
                break;
            case R.id.button8:
                tileToggleButton5.toggle();
                tileToggleButton7.toggle();
                tileToggleButton9.toggle();
                break;
            case R.id.button9:
                tileToggleButton6.toggle();
                tileToggleButton8.toggle();
                break;
        }
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(10000, 1000) {

        public void onTick(long millisUntilFinished) {
            String seconds = ((Long) (millisUntilFinished / 1000)).toString();
            mTimerTextView.setText(seconds);
        }

        public void onFinish() {
            isDefeat();
        }

    };

}