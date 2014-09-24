package com.bignerdranch.android.liferx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class LifeFragment extends Fragment {

    @InjectView(R.id.button1) Button tileButton1;
    @InjectView(R.id.button2) Button tileButton2;
    @InjectView(R.id.button3) Button tileButton3;
    @InjectView(R.id.button4) Button tileButton4;
    @InjectView(R.id.button5) Button tileButton5;
    @InjectView(R.id.button6) Button tileButton6;
    @InjectView(R.id.button7) Button tileButton7;
    @InjectView(R.id.button8) Button tileButton8;
    @InjectView(R.id.button9) Button tileButton9;
    @InjectView(R.id.button_restart) Button buttonRestart;

    private List<Button> tileButtons;

    Subscription _subscription;
    PublishSubject<Button> _resultEmitterSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_life, container, false);
        ButterKnife.inject(this, layout);

        tileButtons = new ArrayList<Button>() {{
            add(tileButton1);
            add(tileButton2);
            add(tileButton3);
            add(tileButton4);
            add(tileButton5);
            add(tileButton6);
            add(tileButton7);
            add(tileButton8);
            add(tileButton9);
        }};

        _resultEmitterSubject = PublishSubject.create();
        _subscription = _resultEmitterSubject.asObservable().subscribe(new Action1<Button>() {
            @Override
            public void call(Button button) {
                button.setEnabled(false);
                // TODO - start timer if not started
                if (isVictory()) {
                    // TODO - stop timer
                    Toast.makeText(getActivity(), getString(R.string.victory), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return layout;
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9})
    public void activate(Button button) {
        _resultEmitterSubject.onNext(button);
    }

    @OnClick(R.id.button_restart)
    public void restart() {
        for (Button button : tileButtons) {
            button.setEnabled(true);
        }
    }

    private boolean isVictory() {
        for (Button button : tileButtons) {
            if (button.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

}