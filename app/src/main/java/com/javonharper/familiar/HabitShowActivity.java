package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HabitShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        Intent intent = getIntent();
        Integer habitId = Integer.valueOf(intent.getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        final HabitController controller = new HabitController(this);
        final Habit habit = controller.getHabit(habitId);

        TextView nameView = (TextView) findViewById(R.id.habit_name);

        nameView.setText(habit.getName());

        Button deleteButton = (Button) findViewById(R.id.delete_habit);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteHabit(habit.getId());

                Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
                startActivity(intent);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
