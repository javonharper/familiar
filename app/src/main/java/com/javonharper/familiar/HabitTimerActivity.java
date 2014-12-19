package com.javonharper.familiar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class HabitTimerActivity extends Activity {

    private static final String SECONDS_REMAINING = "SECONDS_REMAINING";
    Handler handler = new Handler();
    private HabitController controller;
    private Habit habit;
    private TextView habitName;
    private LinearLayout pauseButtonContainer;
    private LinearLayout resumeButtonContainer;
    private LinearLayout doneContainer;
    private TextView stopButton;
    private TextView resumeButton;
    private TextView doneButton;
    private TextView timeLeft;
    private HabitTimer timer = HabitTimer.getInstance();
    private Integer secondsRemaining;
    private Vibrator vibes;
    private Integer TIMER_ID = 0;

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_timer);

        Integer habitId = getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0);

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        secondsRemaining = getIntent().getIntExtra(HabitTimerActivity.SECONDS_REMAINING, habit.getDuration() * 60);

        getActionBar().hide();
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        initializeView();
        vibes = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        updateUI();

        if (!isTimerFinished()) {
            resumeTimer();
        } else {
            notificationManager.cancel(TIMER_ID);
        }

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeTimer();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });
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
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success);
        mediaPlayer.start();
        vibes.vibrate(100);

        timer.stop();
        timeLeft.setText("DONE!");
        timeLeft.setTextColor(getResources().getColor(R.color.green));

        resumeButtonContainer.setVisibility(View.GONE);
        pauseButtonContainer.setVisibility(View.GONE);
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

        Integer minutes = (secondsRemaining % 3600) / 60;
        Integer seconds = secondsRemaining % 60;
        String minutesPadded = String.format("%02d", minutes);
        String secondsPadded = String.format("%02d", seconds);
        String time = minutesPadded + ":" + secondsPadded;

        String contentText;
        if (secondsRemaining <= 0) {
            contentText = "You're Done!";
        } else {
            if (minutes == 0) {
                if (seconds == 1) {
                    contentText = seconds + " second left.";

                } else {
                    contentText = seconds + " seconds left.";

                }

            } else {
                if (minutes == 1) {
                    contentText = minutes + " minute left.";
                } else {
                    contentText = minutes + " minutes left.";

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

        Integer minutes = (secondsRemaining % 3600) / 60;
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
        }
    }

    @Override
    public void onBackPressed() {

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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resumeTimer();
                    }
                })
                .show();
    }

    private void initializeView() {
        timeLeft = (TextView) findViewById(R.id.time_left);
        habitName = (TextView) findViewById(R.id.habit_name);
        stopButton = (TextView) findViewById(R.id.stop_button);
        resumeButton = (TextView) findViewById(R.id.resume_button);
        doneButton = (TextView) findViewById(R.id.done_button);

        pauseButtonContainer = (LinearLayout) findViewById(R.id.pause_container);
        resumeButtonContainer = (LinearLayout) findViewById(R.id.resume_container);
        doneContainer = (LinearLayout) findViewById(R.id.done_container);

        doneContainer.setVisibility(View.GONE);

        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        timeLeft.setTypeface(font);
        habitName.setTypeface(font);
        stopButton.setTypeface(font);
        resumeButton.setTypeface(font);
        doneButton.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
