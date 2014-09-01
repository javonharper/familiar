package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.R;

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

        final TextView nameView = (TextView) findViewById(R.id.name_edit);

        nameView.setText(habit.getName());

        Button doneButton = (Button) findViewById(R.id.done_editing);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = nameView.getText().toString();
                habit.setName(newName);
                controller.updateHabit(habit);

                String message = "Habit \"" + habit.getName() + "\" updated.";
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(view.getContext(), HabitShowActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.habit_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
