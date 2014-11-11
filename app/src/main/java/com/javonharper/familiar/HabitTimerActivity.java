package com.javonharper.familiar;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;


public class HabitTimerActivity extends Activity {

    private HabitController controller;
    private Habit habit;
    private TextView habitName;
    private TextView stopButton;
    private TextView timeLeft;
    Handler handler = new Handler();
    private Timer timer = new Timer();
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

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        secondsRemaining -= 1;

                        Integer minutes = (secondsRemaining % 3600) / 60;
                        Integer seconds = secondsRemaining % 60;
                        String secondsPadded = String.format("%02d", seconds);


                        timeLeft.setText(minutes.toString() + ":" + secondsPadded);
                        System.out.println("Seconds Remaining: " + secondsRemaining);
                    }
                });
            }
        }, 0, 1000);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
            }
        });
    }

    private void initializeView() {
        timeLeft = (TextView) findViewById(R.id.time_left);
        habitName = (TextView) findViewById(R.id.habit_name);
        stopButton = (TextView) findViewById(R.id.stop_button);
        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        timeLeft.setTypeface(font);
        habitName.setTypeface(font);
        stopButton.setTypeface(font);
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
