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

public class HabitShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_show);

        Intent intent = getIntent();
        Integer habitId = Integer.valueOf(intent.getIntExtra(HabitIndexActivity.HABIT_ID, 0));

        final HabitController controller = new HabitController(this);
        final Habit habit = controller.getHabit(habitId);

        setTitle(habit.getName());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView nameView = (TextView) findViewById(R.id.habit_name);

        nameView.setText(habit.getName());

        Button deleteButton = (Button) findViewById(R.id.delete_habit);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteHabit(habit.getId());

                String message = "Habit \"" + habit.getName() + "\" deleted.";
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(view.getContext(), HabitIndexActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                startActivity(intent);
            }
        });

        Button editButton = (Button) findViewById(R.id.edit_habit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HabitEditActivity.class);
                intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
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
