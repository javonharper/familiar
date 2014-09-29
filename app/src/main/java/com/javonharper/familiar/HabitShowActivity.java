package com.javonharper.familiar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HabitShowActivity extends Activity {

    private Habit habit;
    private HabitController controller;
    private TextView currentProgress;
    private TextView durationLabel;
    private TextView timesPerDurationLabel;
    private TextView nameView;
    private TextView durationView;
    private TextView timesPerWeekView;
    private Button increment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        setTitle(habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initializeView();

        nameView.setText(habit.getName());

        Integer duration = habit.getDuration();
        if (duration == null || duration == 0) {
            durationView.setText("Not set");
        } else {
            durationView.setText(duration.toString() + " minutes");
        }

        timesPerWeekView.setText(habit.getTimesPerDuration().toString());
        currentProgress.setText(habit.getCurrentProgress().toString());

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer newCurrentProgress = habit.getCurrentProgress() + 1;
                currentProgress.setText(newCurrentProgress.toString());
                habit.setCurrentProgress(newCurrentProgress);
                controller.updateHabit(habit);
            }
        });
    }

    private void initializeView() {
        timesPerWeekView = (TextView) findViewById(R.id.habit_times_per_week);
        durationView = (TextView) findViewById(R.id.habit_duration);
        currentProgress = (TextView) findViewById(R.id.habit_current_progress);
        nameView = (TextView) findViewById(R.id.name);
        durationLabel = (TextView) findViewById(R.id.duration_label);
        timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        increment = (Button) findViewById(R.id.increment);
        initializeTypefaces();

    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        durationLabel.setTypeface(font);
        timesPerDurationLabel.setTypeface(font);
        increment.setTypeface(font);
        currentProgress.setTypeface(font);
        durationView.setTypeface(font);
        nameView.setTypeface(font);
        timesPerWeekView.setTypeface(font);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.action_edit:
                intent = new Intent(this, HabitEditActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                startActivity(intent);
                return true;

            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setMessage("Delete \"" + habit.getName() + "\"?")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                controller.deleteHabit(habit.getId());

                                String message = "\"" + habit.getName() + "\" deleted.";
                                Toast.makeText(HabitShowActivity.this, message, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(HabitShowActivity.this, HabitIndexActivity.class);
                                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;

            case R.id.action_reset:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.reset_progress_prompt))
                        .setCancelable(true)
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String message = "Progress reset for \"" + habit.getName() + "\".";
                                Toast.makeText(HabitShowActivity.this, message, Toast.LENGTH_SHORT).show();

                                Integer newCurrentProgress = 0;
                                currentProgress.setText(newCurrentProgress.toString());
                                habit.setCurrentProgress(newCurrentProgress);
                                controller.updateHabit(habit);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;

//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
