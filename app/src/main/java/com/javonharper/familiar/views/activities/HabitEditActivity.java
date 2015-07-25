package com.javonharper.familiar.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.models.Habit;
import com.javonharper.familiar.daos.HabitController;
import com.javonharper.familiar.models.HabitFormValidator;
import com.javonharper.familiar.R;

import java.util.Map;

public class HabitEditActivity extends Activity {

    private TextView nameLabel;
    private TextView timesPerDurationLabel;
    private TextView durationLabel;
    private TextView daysLabel;
    private TextView mondayCheckboxLabel;
    private TextView tuesdayCheckboxLabel;
    private TextView wednesdayCheckboxLabel;
    private TextView thursdayCheckboxLabel;
    private TextView fridayCheckboxLabel;
    private TextView saturdayCheckboxLabel;
    private TextView sundayCheckboxLabel;

    private CheckBox mondayCheckbox;
    private CheckBox tuesdayCheckbox;
    private CheckBox wednesdayCheckbox;
    private CheckBox thursdayCheckbox;
    private CheckBox fridayCheckbox;
    private CheckBox saturdayCheckbox;
    private CheckBox sundayCheckbox;

    private EditText nameEdit;
    private EditText timesPerDurationEdit;
    private TextView durationEdit;
    private HabitController controller;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        final Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = new HabitController(this).getHabit(habitId);

        setTitle("Edit " + habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initializeView();

        nameEdit.setText(habit.getName());
        durationEdit.setText(habit.getDuration().toString());
        timesPerDurationEdit.setText(habit.getTimesPerDuration().toString());

        mondayCheckbox.setChecked(habit.getDoOnMonday());
        tuesdayCheckbox.setChecked(habit.getDoOnTuesday());
        wednesdayCheckbox.setChecked(habit.getDoOnWednesday());
        thursdayCheckbox.setChecked(habit.getDoOnThursday());
        fridayCheckbox.setChecked(habit.getDoOnFriday());
        saturdayCheckbox.setChecked(habit.getDoOnSaturday());
        sundayCheckbox.setChecked(habit.getDoOnSunday());
    }

    private void initializeView() {
        nameLabel = (TextView) findViewById(R.id.name_label);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        durationLabel = (TextView) findViewById(R.id.duration_label);
        durationEdit = (TextView) findViewById(R.id.duration_edit);
        timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        timesPerDurationEdit = (EditText) findViewById(R.id.times_per_duration_edit);
        daysLabel = (TextView) findViewById(R.id.days_label);
        mondayCheckboxLabel = (TextView) findViewById(R.id.monday_checkbox_label);
        tuesdayCheckboxLabel = (TextView) findViewById(R.id.tuesday_checkbox_label);
        wednesdayCheckboxLabel = (TextView) findViewById(R.id.wednesday_checkbox_label);
        thursdayCheckboxLabel = (TextView) findViewById(R.id.thursday_checkbox_label);
        fridayCheckboxLabel = (TextView) findViewById(R.id.friday_checkbox_label);
        saturdayCheckboxLabel = (TextView) findViewById(R.id.saturday_checkbox_label);
        sundayCheckboxLabel = (TextView) findViewById(R.id.sunday_checkbox_label);

        mondayCheckbox = (CheckBox) findViewById(R.id.monday_checkbox);
        tuesdayCheckbox = (CheckBox) findViewById(R.id.tuesday_checkbox);
        wednesdayCheckbox = (CheckBox) findViewById(R.id.wednesday_checkbox);
        thursdayCheckbox = (CheckBox) findViewById(R.id.thursday_checkbox);
        fridayCheckbox = (CheckBox) findViewById(R.id.friday_checkbox);
        saturdayCheckbox = (CheckBox) findViewById(R.id.saturday_checkbox);
        sundayCheckbox = (CheckBox) findViewById(R.id.sunday_checkbox);

        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        nameLabel.setTypeface(font);
        nameEdit.setTypeface(font);
        durationLabel.setTypeface(font);
        durationEdit.setTypeface(font);
        timesPerDurationLabel.setTypeface(font);
        timesPerDurationEdit.setTypeface(font);
        daysLabel.setTypeface(font);
        mondayCheckboxLabel.setTypeface(font);
        tuesdayCheckboxLabel.setTypeface(font);
        wednesdayCheckboxLabel.setTypeface(font);
        thursdayCheckboxLabel.setTypeface(font);
        fridayCheckboxLabel.setTypeface(font);
        saturdayCheckboxLabel.setTypeface(font);
        sundayCheckboxLabel.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_edit, menu);
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
                Boolean doOnMonday = mondayCheckbox.isChecked();
                Boolean doOnTuesday = tuesdayCheckbox.isChecked();
                Boolean doOnWednesday = wednesdayCheckbox.isChecked();
                Boolean doOnThursday = thursdayCheckbox.isChecked();
                Boolean doOnFriday = fridayCheckbox.isChecked();
                Boolean doOnSaturday = saturdayCheckbox.isChecked();
                Boolean doOnSunday = sundayCheckbox.isChecked();

                HabitFormValidator validator = new HabitFormValidator(newName, newTimesPerDuration, newDuration);
                validator.validate();

                if (validator.isValid()) {
                    habit.setName(newName);
                    habit.setTimesPerDuration(Integer.valueOf(newTimesPerDuration));
                    habit.setDuration(Integer.valueOf(newDuration));
                    habit.setDoOnMonday(doOnMonday);
                    habit.setDoOnTuesday(doOnTuesday);
                    habit.setDoOnWednesday(doOnWednesday);
                    habit.setDoOnThursday(doOnThursday);
                    habit.setDoOnFriday(doOnFriday);
                    habit.setDoOnSaturday(doOnSaturday);
                    habit.setDoOnSunday(doOnSunday);

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

//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
