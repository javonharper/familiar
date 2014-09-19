package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HabitNewActivity extends Activity {

    private TextView nameLabel;
    private TextView timesPerDurationLabel;
    private TextView durationLabel;
    private TextView minutesLabel;
    private EditText nameEdit;
    private EditText timesPerDurationEdit;
    private TextView durationEdit;
    private Button createButton;
    private Button cancelButton;
    private HabitController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);

        setTitle(R.string.add_habit);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initializeView();

        controller = new HabitController(this);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = nameEdit.getText().toString().trim();
                    Integer timesPerDuration = Integer.valueOf(timesPerDurationEdit.getText().toString().trim());
                    Integer duration = Integer.valueOf(durationEdit.getText().toString().trim());


                    Habit habit = new Habit(null, name, duration, timesPerDuration, 0);
                    controller.createHabit(habit);

                    String message = "Habit \"" + habit.getName() + "\" created.";
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    timesPerDurationEdit.setError(getString(R.string.times_per_duration_validation_message));
                    Toast.makeText(view.getContext(), getString(R.string.times_per_duration_validation_message), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitNewActivity.this.finish();
            }
        });
    }

    private void initializeView() {
        nameLabel = (TextView) findViewById(R.id.name_label);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        durationLabel = (TextView) findViewById(R.id.duration_label);
        minutesLabel = (TextView) findViewById(R.id.minutes_label);
        durationEdit = (TextView) findViewById(R.id.duration_edit);
        timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        timesPerDurationEdit = (EditText) findViewById(R.id.times_per_duration_edit);
        createButton = (Button) findViewById(R.id.create_habit);
        cancelButton = (Button) findViewById(R.id.cancel);
        initializeTypefaces();
    }

    private void initializeTypefaces() {
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        nameLabel.setTypeface(font);
        minutesLabel.setTypeface(font);
        nameEdit.setTypeface(font);
        durationLabel.setTypeface(font);
        durationEdit.setTypeface(font);
        timesPerDurationLabel.setTypeface(font);
        timesPerDurationEdit.setTypeface(font);
        createButton.setTypeface(font);
        cancelButton.setTypeface(font);
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
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
