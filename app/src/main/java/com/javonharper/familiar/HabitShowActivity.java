package com.javonharper.familiar;

import android.app.Activity;
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

public class HabitShowActivity extends Activity {

    private Habit habit;
    private HabitController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        Intent intent = getIntent();
        Integer habitId = Integer.valueOf(intent.getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = controller.getHabit(habitId);

        setTitle(habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));

        TextView nameLabel = (TextView) findViewById(R.id.name_label);
        nameLabel.setTypeface(font);

        TextView timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        timesPerDurationLabel.setTypeface(font);

        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setTypeface(font);
        nameView.setText(habit.getName());

        TextView timesPerWeekView = (TextView) findViewById(R.id.habit_times_per_week);
        timesPerWeekView.setTypeface(font);
        timesPerWeekView.setText(habit.getTimesPerDuration().toString());

        final TextView currentProgress = (TextView) findViewById(R.id.habit_current_progress);
        currentProgress.setTypeface(font);
        currentProgress.setText(habit.getCurrentProgress().toString());

        Button increment = (Button) findViewById(R.id.increment);
        increment.setTypeface(font);
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer newCurrentProgress = habit.getCurrentProgress() + 1;
                currentProgress.setText(newCurrentProgress.toString());
                habit.setCurrentProgress(newCurrentProgress);
                controller.updateHabit(habit);
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setTypeface(font);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Are you sure you want to reset your progress?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Integer newCurrentProgress = 0;
                                currentProgress.setText(newCurrentProgress.toString());
                                habit.setCurrentProgress(newCurrentProgress);
                                controller.updateHabit(habit);
                            }
                        })
                .setNegativeButton("No", null)
                .show();
            }
        });
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
                        .setMessage("Are you sure you want to delete \"" + habit.getName() + "\"?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("No", null)
                        .show();
                return true;

            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
