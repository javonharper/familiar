package com.javonharper.familiar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Comparator;
import java.util.List;


public class HabitIndexActivity extends Activity {

    public static String HABIT_ID = "habitId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_index);

        HabitController controller = new HabitController(this);
        List<Habit> habits = controller.getAllHabits();
        HabitsAdapter adapter = new HabitsAdapter(this, habits);
        adapter.sort(new HabitComparator());
        final ListView habitListView = (ListView) findViewById(R.id.habit_list);
        habitListView.setAdapter(adapter);

        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) habitListView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), HabitShowActivity.class);
                intent.putExtra(HABIT_ID, habit.getId().intValue());
                startActivity(intent);
            }
        });

        Button newHabitButton = (Button) findViewById(R.id.new_habit_button);

        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));
        newHabitButton.setTypeface(font);

        newHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HabitNewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.habit_index, menu);
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