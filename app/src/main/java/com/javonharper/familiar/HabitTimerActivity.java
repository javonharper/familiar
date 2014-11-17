package com.javonharper.familiar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
    private Timer timer;
    private Integer secondsRemaining;
    private Vibrator vibes;
    private Integer TIMER_ID = 9001;
    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_timer);

        Integer habitId = getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0);

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        secondsRemaining = Integer.valueOf(getIntent().getIntExtra(HabitTimerActivity.SECONDS_REMAINING, habit.getDuration() * 60));

        getActionBar().hide();
        initializeView();
        vibes = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        habitName.setText(habit.getName());

        resumeTimer();

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

                String message = "Nice! Your progress has been updated.";
                Toast.makeText(HabitTimerActivity.this, message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        this.finish();
        super.onDestroy();
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

        timer.cancel();
        notificationManager.cancel(TIMER_ID);

        timer = null;

    }

    private void timerDone() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success);
        mediaPlayer.start();
        vibes.vibrate(100);

        timer.cancel();
        timeLeft.setText("Done!");
        timeLeft.setTextColor(getResources().getColor(R.color.green));

        resumeButtonContainer.setVisibility(View.GONE);
        pauseButtonContainer.setVisibility(View.GONE);
        doneContainer.setVisibility(View.VISIBLE);

        notificationManager.cancel(TIMER_ID);
    }


    private void resumeTimer() {
        resumeButtonContainer.setVisibility(View.GONE);
        pauseButtonContainer.setVisibility(View.VISIBLE);

        timeLeft.clearAnimation();
        habitName.clearAnimation();

        if (timer == null) {
            timer = new Timer();
        }

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Integer minutes = (secondsRemaining % 3600) / 60;
                            Integer seconds = secondsRemaining % 60;
                            String minutesPadded = String.format("%02d", minutes);
                            String secondsPadded = String.format("%02d", seconds);
                            String time = minutesPadded + ":" + secondsPadded;

                            timeLeft.setText(time);

                            Intent intent = new Intent(getApplicationContext(), HabitTimerActivity.class);
                            intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                            intent.putExtra(HabitTimerActivity.SECONDS_REMAINING, seconds);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            notificationBuilder.setContentIntent(contentIntent);
                            notificationBuilder.setContentText("Time left: " + time);

                            notificationManager.notify(TIMER_ID, notificationBuilder.build());

                            secondsRemaining -= 1;

                            if (secondsRemaining == -1) {
                                timerDone();
                            }
                        }
                    });
                }

            }, 0, 1000);

            notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Timer Running: " + habit.getName())
                    .setContentText("Timer starting..");

            Intent intent = new Intent(this, HabitTimerActivity.class);
            intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);

            notificationManager.notify(TIMER_ID, notificationBuilder.build());


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
