package com.bignerdranch.android.liferx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    @InjectView(R.id.button_restart) Button buttonRestart;

    private List<ToggleButton> tileButtons;

    Subscription mSubscription;
    PublishSubject<ToggleButton> mTileClickSubject;

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

        mTileClickSubject = PublishSubject.create();
        mSubscription = mTileClickSubject.asObservable().subscribe(new Action1<ToggleButton>() {
            @Override
            public void call(ToggleButton button) {
                toggleAdjacent(button);
                if (isVictory()) {
                    disable();
                    Toast.makeText(getActivity(), getString(R.string.victory), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return layout;
    }

    private void toggleAdjacent(ToggleButton toggleButton) {
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

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9})
    public void activate(ToggleButton toggleButton) {
        mTileClickSubject.onNext(toggleButton);
    }

    @OnClick(R.id.button_restart)
    public void restart() {
        for (ToggleButton toggleButton : tileButtons) {
            toggleButton.setEnabled(true);
            toggleButton.setChecked(false);
        }
    }

    private boolean isVictory() {
        for (ToggleButton toggleButton : tileButtons) {
            if (!toggleButton.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private void disable() {
        for (ToggleButton toggleButton : tileButtons) {
            toggleButton.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

}