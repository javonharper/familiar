package com.javonharper.familiar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.Habit;
import com.javonharper.familiar.HabitController;
import com.javonharper.familiar.HabitFormValidator;
import com.javonharper.familiar.R;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HabitEditActivity extends BaseActivity {
    @Bind(R.id.name_label) TextView nameLabel;
    @Bind(R.id.times_per_duration_label) TextView timesPerDurationLabel;
    @Bind(R.id.duration_label) TextView durationLabel;
    @Bind(R.id.name_edit) EditText nameEdit;
    @Bind(R.id.times_per_duration_edit) EditText timesPerDurationEdit;
    @Bind(R.id.duration_edit) TextView durationEdit;


    private HabitController controller;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);

        ButterKnife.bind(this);

        final Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = new HabitController(this).getHabit(habitId);

        setTitle("Edit " + habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        hideActionBarIcon();
        nameEdit.setText(habit.getName());
        durationEdit.setText(habit.getDuration().toString());
        timesPerDurationEdit.setText(habit.getTimesPerDuration().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case android.R.id.home:
                intent = new Intent(HabitEditActivity.this, HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.action_cancel:
                HabitEditActivity.this.finish();
                return true;

            case R.id.action_create:
                String newName = nameEdit.getText().toString().trim();
                String newTimesPerDuration = timesPerDurationEdit.getText().toString().trim();
                String newDuration = durationEdit.getText().toString().trim();

                HabitFormValidator validator = new HabitFormValidator(newName, newTimesPerDuration, newDuration);
                validator.validate();

                if (validator.isValid()) {
                    habit.setName(newName);
                    habit.setTimesPerDuration(Integer.valueOf(newTimesPerDuration));
                    habit.setDuration(Integer.valueOf(newDuration));

                    controller.updateHabit(habit);

                    String message = "Habit \"" + habit.getName() + "\" updated.";
                    Toast.makeText(HabitEditActivity.this, message, Toast.LENGTH_SHORT).show();

                    intent = new Intent(HabitEditActivity.this, HabitIndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Map<String, String> errors = validator.getErrors();
                    if (errors.get(HabitFormValidator.NAME) != null) {
                        nameEdit.setError(errors.get(HabitFormValidator.NAME));
                    }

                    if (errors.get(HabitFormValidator.TIMES_PER_DURATION) != null) {
                        timesPerDurationEdit.setError(errors.get(HabitFormValidator.TIMES_PER_DURATION));
                    }

                    if (errors.get(HabitFormValidator.DURATION) != null) {
                        durationEdit.setError(errors.get(HabitFormValidator.DURATION));
                    }
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
