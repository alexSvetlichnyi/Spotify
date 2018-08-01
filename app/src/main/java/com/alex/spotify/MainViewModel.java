package com.alex.spotify;

import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {
    public static final String MM_SS_SSS = "mm:ss:SSS";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public ObservableField<String> counter = new ObservableField<>();
    private AtomicLong miliseconds = new AtomicLong(0);
    private AtomicBoolean isPaused = new AtomicBoolean(true);
    private boolean isStarted = false;

    public MainViewModel() {
        counter.set(convertMiliSeconds(miliseconds.get()));
    }

    public void onStartStopClick(View view) {
        isPaused.set(!isPaused.get());
        if (!isStarted) {
            compositeDisposable.add(io.reactivex.Observable.interval(1, TimeUnit.MILLISECONDS).
                    doOnSubscribe(disposable -> isStarted = true).
                    filter(aLong -> !isPaused.get()).
                    subscribe(aLong -> counter.set(convertMiliSeconds(miliseconds.getAndIncrement()))));
        }
    }

    public void onResetClick(View view) {
        miliseconds.set(0);
        counter.set(convertMiliSeconds(miliseconds.get()));
        isPaused.set(true);
        Collections.singletonList("test");
    }

    private String convertMiliSeconds(long miliseconds) {
        Date date = new Date(miliseconds);
        SimpleDateFormat formatter= new SimpleDateFormat(MM_SS_SSS, Locale.getDefault());
        return formatter.format(date);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
