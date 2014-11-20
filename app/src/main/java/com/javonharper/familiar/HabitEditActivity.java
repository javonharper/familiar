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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class HabitEditActivity extends Activity {

    private TextView nameLabel;
    private TextView timesPerDurationLabel;
    private TextView durationLabel;

    private EditText nameEdit;
    private EditText timesPerDurationEdit;
    private TextView durationEdit;
    private Button doneButton;
    private HabitController controller;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);

        final Integer habitId = Integer.valueOf(getIntent().getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        controller = new HabitController(this);
        habit = new HabitController(this).getHabit(habitId);

        setTitle("Edit " + habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        initializeView();

        nameEdit.setText(habit.getName());
        durationEdit.setText(habit.getDuration().toString());
        timesPerDurationEdit.setText(habit.getTimesPerDuration().toString());

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
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
            }
        });
    }

    private void initializeView() {
        nameLabel = (TextView) findViewById(R.id.name_label);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        durationLabel = (TextView) findViewById(R.id.duration_label);
        durationEdit = (TextView) findViewById(R.id.duration_edit);
        timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        timesPerDurationEdit = (EditText) findViewById(R.id.times_per_duration_edit);
        doneButton = (Button) findViewById(R.id.done_editing);

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
        doneButton.setTypeface(font);
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
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(HabitEditActivity.this, HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.action_cancel:
                HabitEditActivity.this.finish();

//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
