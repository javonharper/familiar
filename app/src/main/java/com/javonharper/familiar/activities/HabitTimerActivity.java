package com.javonharper.familiar.activities;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.Habit;
import com.javonharper.familiar.HabitController;
import com.javonharper.familiar.HabitTimer;
import com.javonharper.familiar.R;

import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HabitTimerActivity extends BaseActivity {
    @Bind(R.id.habit_name)
    TextView habitName;
    @Bind(R.id.pause_container)
    LinearLayout pauseButtonContainer;
    @Bind(R.id.resume_container)
    LinearLayout resumeButtonContainer;
    @Bind(R.id.done_container)
    LinearLayout doneContainer;
    @Bind(R.id.timer_active_container)
    LinearLayout timerActiveContainer;
    @Bind(R.id.pause_button)
    TextView stopButton;
    @Bind(R.id.resume_button)
    TextView resumeButton;
    @Bind(R.id.done_button)
    TextView doneButton;
    @Bind(R.id.fast_quit_button)
    TextView fastQuitButton;
    @Bind(R.id.time_left)
    TextView timeLeft;

    private HabitController controller;
    private Habit habit;

    private static final String SECONDS_REMAINING = "SECONDS_REMAINING";
    Handler handler = new Handler();

    private HabitTimer timer = HabitTimer.getInstance();
    private Integer secondsRemaining;
    private Integer TIMER_ID = 0;

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_timer);

        ButterKnife.bind(this);

        getActionBar().hide();

        Integer habitId = getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0);

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        secondsRemaining = getIntent().getIntExtra(HabitTimerActivity.SECONDS_REMAINING, habit.getDuration() * 60);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        updateUI();

        if (!isTimerFinished()) {
            resumeTimer();
        } else {
            notificationManager.cancel(TIMER_ID);
        }
    }

    private boolean isTimerFinished() {
        return secondsRemaining <= 0;
    }

    private void pauseTimer() {
        pauseButtonContainer.setVisibility(View.GONE);
        resumeButtonContainer.setVisibility(View.VISIBLE);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        anim.setStartOffset(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        timeLeft.startAnimation(anim);
        habitName.startAnimation(anim);

        timer.stop();
        notificationManager.cancel(TIMER_ID);
    }

    private void timerDone() {
        playSound();
        vibrate();

        timer.stop();
        timeLeft.setText("DONE!");
        timeLeft.setTextColor(getResources().getColor(R.color.green));

        resumeButtonContainer.setVisibility(View.GONE);
        pauseButtonContainer.setVisibility(View.GONE);
        timerActiveContainer.setVisibility(View.GONE);
        doneContainer.setVisibility(View.VISIBLE);
    }

    private void resumeTimer() {
        resumeButtonContainer.setVisibility(View.GONE);
        pauseButtonContainer.setVisibility(View.VISIBLE);

        timeLeft.clearAnimation();
        habitName.clearAnimation();

        timer.startTimer(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        updateUI();
                        updateNotification();
                    }
                });
            }

        }, 0, 1000);

        initNotification();
    }

    private void initNotification() {
        notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(habit.getName())
                .setContentText("Timer starting..");

        Intent intent = new Intent(this, HabitTimerActivity.class);
        intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(TIMER_ID, notification);
    }

    private void updateNotification() {
        Intent intent = new Intent(this, HabitTimerActivity.class);
        intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
        intent.putExtra(HabitTimerActivity.SECONDS_REMAINING, secondsRemaining);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setWhen(0);

        Integer minutes = (secondsRemaining % 3600) / 60;

        String contentText;
        if (secondsRemaining <= 0) {
            contentText = "You're Done!";
        } else {
            if (minutes == 0) {
                contentText = "Less than a minute remaining";
            } else {
                if (minutes == 1) {
                    contentText = minutes + " minute left";
                } else {
                    contentText = minutes + " minutes left";

                }
            }
        }

        notificationBuilder.setContentText(contentText);

        Notification notification = notificationBuilder.build();

        secondsRemaining -= 1;

        if (secondsRemaining == -1) {
            timerDone();
        } else {
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        notificationManager.notify(TIMER_ID, notification);
    }

    private void updateUI() {
        habitName.setText(habit.getName());

        Integer minutes = secondsRemaining / 60;
        Integer seconds = secondsRemaining % 60;
        String minutesPadded = String.format("%02d", minutes);
        String secondsPadded = String.format("%02d", seconds);
        String time = minutesPadded + ":" + secondsPadded;

        timeLeft.setText(time);

        if (timer.getIsRunning()) {
            pauseButtonContainer.setVisibility(View.VISIBLE);
            resumeButtonContainer.setVisibility(View.GONE);
            doneContainer.setVisibility(View.GONE);
        } else {
            pauseButtonContainer.setVisibility(View.GONE);
            resumeButtonContainer.setVisibility(View.GONE);
            doneContainer.setVisibility(View.VISIBLE);
        }

        if (isTimerFinished()) {
            timeLeft.setText("DONE!");
            timerActiveContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (isTimerFinished()) {
            Intent intent = new Intent(this, HabitShowActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId());
            startActivity(intent);
        } else {

            pauseTimer();

            new AlertDialog.Builder(HabitTimerActivity.this)
                    .setMessage("Going back will quit your session")
                    .setCancelable(true)
                    .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            notificationManager.cancel(TIMER_ID);
                            finish();
                            HabitTimerActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resumeTimer();
                        }
                    })
                    .show();
        }

    }

    private void playSound() {
        MediaPlayer.create(this, R.raw.success).start();
    }

    private void vibrate() {
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
    }

    @OnClick(R.id.pause_button)
    void onPauseButtonClicked(View view) {
        pauseTimer();
    }

    @OnClick(R.id.resume_button)
    void onResumeButtonClicked(View view) {
        resumeTimer();
    }

    @OnClick(R.id.fast_quit_button)
    void onFastQuitButtonClicked(View view) {
        pauseTimer();
        new AlertDialog.Builder(HabitTimerActivity.this)
                .setMessage("Do you really want to quit your session?")
                .setCancelable(true)
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        notificationManager.cancel(TIMER_ID);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resumeTimer();
                    }
                })
                .show();
    }

    @OnClick(R.id.done_button)
    public void onDoneButtonClicked(View view) {
        Integer newCurrentProgress = habit.getCurrentProgress() + 1;
        habit.setCurrentProgress(newCurrentProgress);
        controller.updateHabit(habit);

        notificationManager.cancel(TIMER_ID);


        String message = "Nice! Your progress has been updated.";
        Toast.makeText(HabitTimerActivity.this, message, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(HabitTimerActivity.this, HabitIndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
