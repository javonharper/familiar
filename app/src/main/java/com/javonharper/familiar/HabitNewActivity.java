package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class HabitNewActivity extends Activity {

    private TextView nameLabel;
    private EditText nameEdit;
    private TextView timesPerDurationLabel;
    private EditText timesPerDurationEdit;
    private TextView durationLabel;
    private TextView daysLabel;
    private TextView mondayCheckboxLabel;
    private TextView tuesdayCheckboxLabel;
    private TextView wednesdayCheckbox;
    private TextView thursdayCheckbox;
    private TextView fridayCheckboxLabel;
    private TextView saturdayCheckboxLabel;
    private TextView sundayCheckboxLabel;
    private TextView durationEdit;
    private HabitController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        setTitle(R.string.add_habit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initializeView();

        controller = new HabitController(this);
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
        wednesdayCheckbox = (TextView) findViewById(R.id.wednesday_checkbox_label);
        thursdayCheckbox = (TextView) findViewById(R.id.thursday_checkbox_label);
        fridayCheckboxLabel = (TextView) findViewById(R.id.friday_checkbox_label);
        saturdayCheckboxLabel = (TextView) findViewById(R.id.saturday_checkbox_label);
        sundayCheckboxLabel = (TextView) findViewById(R.id.sunday_checkbox_label);

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
        wednesdayCheckbox.setTypeface(font);
        thursdayCheckbox.setTypeface(font);
        fridayCheckboxLabel.setTypeface(font);
        saturdayCheckboxLabel.setTypeface(font);
        sundayCheckboxLabel.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_new, menu);
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


//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}