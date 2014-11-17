package com.javonharper.familiar;

import java.util.Timer;
import java.util.TimerTask;

public class HabitTimer {
    private static HabitTimer instance;
    Timer timer;
    Boolean isRunning = false;

    private HabitTimer() {

    }

    public static HabitTimer getInstance() {
        if (instance == null) {
            instance = new HabitTimer();
        }
        return instance;
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }

        timer = null;
        isRunning = false;
    }

    public void startTimer(TimerTask task, int delay, int interval) {
        if (isRunning == true) {
            this.stop();
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.scheduleAtFixedRate(task, delay, interval);
        isRunning = true;
    }
}
