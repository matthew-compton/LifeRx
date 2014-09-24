package com.bignerdranch.android.liferx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class LifeFragment extends Fragment {

    @InjectView(R.id.button1) Button _button1;
    @InjectView(R.id.button2) Button _button2;
    @InjectView(R.id.button3) Button _button3;
    @InjectView(R.id.button4) Button _button4;
    @InjectView(R.id.button5) Button _button5;
    @InjectView(R.id.button6) Button _button6;
    @InjectView(R.id.button7) Button _button7;
    @InjectView(R.id.button8) Button _button8;
    @InjectView(R.id.button9) Button _button9;

    Subscription _subscription;
    PublishSubject<Button> _resultEmitterSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_life, container, false);
        ButterKnife.inject(this, layout);

        _resultEmitterSubject = PublishSubject.create();
        _subscription = _resultEmitterSubject.asObservable().subscribe(new Action1<Button>() {
            @Override
            public void call(Button button) {
                button.setEnabled(false);
            }
        });

        return layout;
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9})
    public void activate(Button button) {
        _resultEmitterSubject.onNext(button);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

}