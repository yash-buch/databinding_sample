package com.binc.databindingsample;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.os.Handler;
import android.view.View;

public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> hour = new MutableLiveData();
    private MutableLiveData<Integer> minute = new MutableLiveData();
    private MutableLiveData<Integer> seconds = new MutableLiveData();

    public MutableLiveData<Boolean> startEnabled = new MutableLiveData();
    public MutableLiveData<Boolean> stopEnabled = new MutableLiveData();
    public boolean stopTimer = false;

    public MutableLiveData<String> time = new MutableLiveData();

    private Handler timeTicker = new Handler();

    public MainViewModel() {
        time.setValue("00"+":"+"00"+":"+"00");
        startEnabled.setValue(true);
        stopEnabled.setValue(false);
    }

    public void startTimer() {
        hour.setValue(0);
        minute.setValue(0);
        seconds.setValue(0);
        startEnabled.setValue(false);
        stopEnabled.setValue(true);
        stopTimer = false;
        timeTicker.postDelayed(new TickerRunnable(), 1 * 1000);
    }

    public void stopTimer() {
        startEnabled.setValue(true);
        stopEnabled.setValue(false);
        stopTimer = true;
        hour.setValue(0);
        minute.setValue(0);
        seconds.setValue(0);
        timeTicker.removeCallbacksAndMessages(null);
        time.setValue("00"+":"+"00"+":"+"00");
    }

    private class TickerRunnable implements Runnable {

        @Override
        public void run() {
            String h, m, s;
            int hr = hour.getValue();
            int min = minute.getValue();
            int sec = seconds.getValue() + 1;
            if (sec >= 60) {
                sec = 0;
                seconds.setValue(0);
                min = minute.getValue() + 1;
            }
            if(min >= 60) {
                min = 0;
                minute.setValue(0);
                hr = hour.getValue() + 1;
            }
            if(hr >= 24) {
                hr = 0;
                hour.setValue(0);
            }
            hour.setValue(hr);
            minute.setValue(min);
            seconds.setValue(sec);
            h = getInTwoDigit(hr);
            m = getInTwoDigit(min);
            s = getInTwoDigit(sec);
            time.setValue(h + ":" + m + ":" + s);
            if (!stopTimer)
                timeTicker.postDelayed(new TickerRunnable(), 1 * 1000);
        }

        private String getInTwoDigit(int val) {
            String ret = String.valueOf(val);
            if (val % 10 == val) {
                ret = '0' + String.valueOf(val);
            }
            return ret;
        }
    }

    @BindingAdapter("app:isStartEnabled")
    public static void isStartEnabled(View view, boolean isStartEnabled) {
        view.setEnabled(isStartEnabled);
    }

    @BindingAdapter("app:isStopEnabled")
    public static void isStopEnabled(View view, boolean isStopEnabled) {
        view.setEnabled(isStopEnabled);
    }

}
