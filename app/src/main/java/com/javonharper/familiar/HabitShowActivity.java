package com.javonharper.familiar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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

    private TextView nameLabel;
    private TextView durationValue;
    private TextView durationLabel;
    private TextView frequencyValue;
    private TextView frequencyLabel;
    private TextView currentProgressLabel;
    private TextView currentProgressValue;
    private TextView thisWeek;


    private Button incrementProgressButton;
    private Button startTimerButton;
    private Button resetProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        initializeView();

        nameLabel.setText(habit.getName());

        Integer duration = habit.getDuration();

        if (duration == null || duration == 0) {
            durationValue.setText("Not set");
            durationLabel.setText("");
        } else {
            durationValue.setText(duration.toString());
        }

        frequencyValue.setText(habit.getTimesPerDuration().toString());

        updateCurrentProgress();

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitShowActivity.this, HabitTimerActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                startActivity(intent);
            }
        });

        incrementProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer newCurrentProgress = habit.getCurrentProgress() + 1;
                currentProgressValue.setText(newCurrentProgress.toString());
                habit.setCurrentProgress(newCurrentProgress);
                updateCurrentProgress();
                controller.updateHabit(habit);
            }
        });

        resetProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HabitShowActivity.this)
                        .setMessage(getString(R.string.reset_progress_prompt))
                        .setCancelable(true)
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String message = "Progress reset for \"" + habit.getName() + "\".";
                                Toast.makeText(HabitShowActivity.this, message, Toast.LENGTH_SHORT).show();

                                Integer newCurrentProgress = 0;
                                currentProgressValue.setText(newCurrentProgress.toString());
                                habit.setCurrentProgress(newCurrentProgress);
                                updateCurrentProgress();
                                controller.updateHabit(habit);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    private void updateCurrentProgress() {
        if (habit.getCurrentProgress().equals(0)) {
            currentProgressValue.setText("Not started yet");
            currentProgressValue.setTextColor(getResources().getColor(R.color.black));
            thisWeek.setText("this week");
        } else if (habit.getCurrentProgress() == habit.getTimesPerDuration()) {
            currentProgressValue.setText("Done!");
            currentProgressValue.setTextColor(getResources().getColor(R.color.green));
            thisWeek.setText("for this week");
        } else if (habit.getCurrentProgress() > habit.getTimesPerDuration()) {
            currentProgressValue.setText("Done! (" + habit.getCurrentProgress() + "/" + habit.getTimesPerDuration() + ")");
            currentProgressValue.setTextColor(getResources().getColor(R.color.green));
            thisWeek.setText("times this week");
        } else if (habit.getCurrentProgress() == 1) {
            currentProgressValue.setText(habit.getCurrentProgress().toString());
            thisWeek.setText("time this week");
        } else {
            currentProgressValue.setText(habit.getCurrentProgress().toString());
            thisWeek.setText("times this week");

        }
    }

    private void initializeView() {
        durationLabel = (TextView) findViewById(R.id.duration_label);
        durationValue = (TextView) findViewById(R.id.duration_value);
        frequencyLabel = (TextView) findViewById(R.id.frequency_label);
        frequencyValue = (TextView) findViewById(R.id.frequency_value);
        currentProgressLabel = (TextView) findViewById(R.id.habit_current_progress_label);
        currentProgressValue = (TextView) findViewById(R.id.habit_current_progress);
        nameLabel = (TextView) findViewById(R.id.name);
        startTimerButton = (Button) findViewById(R.id.start_timer_button);
        incrementProgressButton = (Button) findViewById(R.id.increment_progress_button);
        resetProgressButton = (Button) findViewById(R.id.reset_progress);
        thisWeek = (TextView) findViewById(R.id.times_this_week);

        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        currentProgressLabel.setTypeface(font);
        currentProgressValue.setTypeface(font);
        nameLabel.setTypeface(font);
        durationLabel.setTypeface(font);
        durationValue.setTypeface(font);
        frequencyLabel.setTypeface(font);
        frequencyValue.setTypeface(font);
        startTimerButton.setTypeface(font);
        incrementProgressButton.setTypeface(font);
        resetProgressButton.setTypeface(font);
        thisWeek.setTypeface(font);
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
                intent = new Intent(HabitShowActivity.this, HabitIndexActivity.class);
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
        }
        return super.onOptionsItemSelected(item);
    }

}
