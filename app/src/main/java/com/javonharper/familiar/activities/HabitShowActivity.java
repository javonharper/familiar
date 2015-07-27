package com.javonharper.familiar.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.Habit;
import com.javonharper.familiar.HabitController;
import com.javonharper.familiar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HabitShowActivity extends BaseActivity {
    @Bind(R.id.name_label) TextView nameLabel;
    @Bind(R.id.duration_label) TextView durationLabel;
    @Bind(R.id.duration_value) TextView durationValue;
    @Bind(R.id.frequency_label) TextView frequencyLabel;
    @Bind(R.id.frequency_value) TextView frequencyValue;
    @Bind(R.id.habit_current_progress) TextView currentProgressValue;
    @Bind(R.id.times_this_week) TextView thisWeek;
    @Bind(R.id.increment_progress_button) Button incrementProgressButton;
    @Bind(R.id.start_timer_button) Button startTimerButton;
    @Bind(R.id.reset_progress) Button resetProgressButton;

    private HabitController controller;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        ButterKnife.bind(this);
        initializeTypefaces();

        hideActionBarIcon();

        Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        setTitle(habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
                        .setMessage(getString(R.string.alert_message_reset_progress))
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
            thisWeek.setText("sessions completed this week");
        } else if (habit.getCurrentProgress() == 1) {
            currentProgressValue.setText(habit.getCurrentProgress().toString());
            thisWeek.setText("session completed this week");
        } else {
            currentProgressValue.setText(habit.getCurrentProgress().toString());
            thisWeek.setText("sessions completed this week");

        }
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
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
