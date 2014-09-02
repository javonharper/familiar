package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HabitEditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);

        Intent intent = getIntent();

        Integer habitId = Integer.valueOf(intent.getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        final HabitController controller = new HabitController(this);

        final Habit habit = controller.getHabit(habitId);

        setTitle("Edit " + habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));

        TextView nameLabel = (TextView) findViewById(R.id.name_label);
        nameLabel.setTypeface(font);
        TextView timesPerDurationLabel = (TextView) findViewById(R.id.times_per_duration_label);
        timesPerDurationLabel.setTypeface(font);

        final TextView nameEdit = (TextView) findViewById(R.id.name_edit);
        nameEdit.setTypeface(font);
        nameEdit.setText(habit.getName());

        final TextView timesPerDurationEdit = (TextView) findViewById(R.id.times_per_duration_edit);
        timesPerDurationEdit.setTypeface(font);
        timesPerDurationEdit.setText(habit.getTimesPerDuration().toString());

        Button doneButton = (Button) findViewById(R.id.done_editing);
        doneButton.setTypeface(font);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String newName = nameEdit.getText().toString().trim();
                    Integer newTimesPerDuration = Integer.valueOf(timesPerDurationEdit.getText().toString().trim());

                    habit.setName(newName);
                    habit.setTimesPerDuration(newTimesPerDuration);

                    controller.updateHabit(habit);

                    String message = "Habit \"" + habit.getName() + "\" updated.";
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);

                    startActivity(intent);
                } catch (NumberFormatException e) {
                    timesPerDurationEdit.setError(getString(R.string.times_per_duration_validation_message));
                    Toast.makeText(view.getContext(), getString(R.string.times_per_duration_validation_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setTypeface(font);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HabitShowActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), HabitIndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
