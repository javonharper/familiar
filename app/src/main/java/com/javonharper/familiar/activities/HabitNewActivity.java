package com.javonharper.familiar.activities;

import android.content.Intent;
import android.graphics.Typeface;
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

public class HabitNewActivity extends BaseActivity {

    @Bind(R.id.name_label) TextView nameLabel;
    @Bind(R.id.name_edit) EditText nameEdit;
    @Bind(R.id.times_per_duration_label) TextView timesPerDurationLabel;
    @Bind(R.id.times_per_duration_edit) EditText timesPerDurationEdit;
    @Bind(R.id.duration_label) TextView durationLabel;
    @Bind(R.id.duration_edit) TextView durationEdit;

    private HabitController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);

        ButterKnife.bind(this);
        initializeTypefaces();

        hideActionBarIcon();
        setTitle(R.string.add_habit);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        controller = new HabitController(this);
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        nameLabel.setTypeface(font);
        nameEdit.setTypeface(font);
        durationLabel.setTypeface(font);
        durationEdit.setTypeface(font);
        timesPerDurationLabel.setTypeface(font);
        timesPerDurationEdit.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case android.R.id.home:
                intent = new Intent(HabitNewActivity.this, HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.action_cancel:
                HabitNewActivity.this.finish();
                return true;

            case R.id.action_create:
                String newName = nameEdit.getText().toString().trim();
                String newTimesPerDuration = timesPerDurationEdit.getText().toString().trim();
                String newDuration = durationEdit.getText().toString().trim();

                HabitFormValidator validator = new HabitFormValidator(newName, newTimesPerDuration, newDuration);
                validator.validate();

                if (validator.isValid()) {
                    Habit habit = new Habit();
                    habit.setName(newName);
                    habit.setTimesPerDuration(Integer.valueOf(newTimesPerDuration));
                    habit.setDuration(Integer.valueOf(newDuration));

                    controller.createHabit(habit);

                    String message = "Habit \"" + habit.getName() + "\" created.";
                    Toast.makeText(HabitNewActivity.this, message, Toast.LENGTH_SHORT).show();

                    intent = new Intent(HabitNewActivity.this, HabitIndexActivity.class);
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