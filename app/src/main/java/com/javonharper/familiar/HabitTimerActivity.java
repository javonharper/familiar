package com.javonharper.familiar;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class HabitTimerActivity extends Activity {

    Handler handler = new Handler();
    private HabitController controller;
    private Habit habit;
    private TextView habitName;
    private LinearLayout pauseButtonContainer;
    private LinearLayout resumeButtonContainer;
    private TextView stopButton;
    private TextView resumeButton;
    private TextView timeLeft;
    private Timer timer;
    private Integer secondsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_timer);

        Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        secondsRemaining = habit.getDuration() * 60;

        getActionBar().hide();
        initializeView();

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
        timer = null;
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
                        secondsRemaining -= 1;

                        Integer minutes = (secondsRemaining % 3600) / 60;
                        Integer seconds = secondsRemaining % 60;
                        String minutesPadded = String.format("%02d", minutes);
                        String secondsPadded = String.format("%02d", seconds);

                        timeLeft.setText(minutesPadded + ":" + secondsPadded);
                    }
                });
            }
        }, 0, 1000);
    }

    private void initializeView() {
        timeLeft = (TextView) findViewById(R.id.time_left);
        habitName = (TextView) findViewById(R.id.habit_name);
        stopButton = (TextView) findViewById(R.id.stop_button);
        resumeButton = (TextView) findViewById(R.id.resume_button);

        pauseButtonContainer = (LinearLayout) findViewById(R.id.pause_container);
        resumeButtonContainer = (LinearLayout) findViewById(R.id.resume_container);

        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        timeLeft.setTypeface(font);
        habitName.setTypeface(font);
        stopButton.setTypeface(font);
        resumeButton.setTypeface(font);

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
