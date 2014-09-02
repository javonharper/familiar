package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.javonharper.familiar.R;

public class HabitNewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_new);

        setTitle(R.string.new_habit);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        final HabitController controller = new HabitController(this);

        Button createButton = (Button) findViewById(R.id.create_habit);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEdit = (EditText) findViewById(R.id.name_edit);
                EditText timesPerDurationEdit = (EditText) findViewById(R.id.times_per_duration_edit);

                try {
                    String name = nameEdit.getText().toString().trim();
                    Integer timesPerDuration = Integer.valueOf(timesPerDurationEdit.getText().toString().trim());

                    Habit habit = new Habit(null, name, timesPerDuration);
                    controller.createHabit(habit);

                    String message = "Habit \"" + habit.getName() + "\" created.";
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    timesPerDurationEdit.setError(getString(R.string.times_per_duration_validation_message));
                    Toast.makeText(view.getContext(), getString(R.string.times_per_duration_validation_message), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
                startActivity(intent);
            }
        });
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
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
