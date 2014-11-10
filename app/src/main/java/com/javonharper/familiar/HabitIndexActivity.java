package com.javonharper.familiar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HabitIndexActivity extends Activity {

    public static String HABIT_ID = "habitId";
    private List<Habit> habits;
    private HabitController controller;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp = false;
    FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_index);
        controller = new HabitController(this);
        initializeFABButton();
    }

    private void initializeFABButton() {
        fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_plus ))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HabitNewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        habits = controller.getAllHabits();

        if (habits.size() == 0) {
            showEmptyStateView();
        }

        Collections.sort(habits, new HabitComparator());
        HabitsAdapter adapter = new HabitsAdapter(this, habits);

        final ListView habitListView = (ListView) findViewById(R.id.habit_list);
        habitListView.setAdapter(adapter);


        habitListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final ListView lw = habitListView;

                if (view.getId() == lw.getId()) {
                    final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        fabButton.hideFloatingActionButton();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        fabButton.showFloatingActionButton();

                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });

        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) habitListView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), HabitShowActivity.class);
                intent.putExtra(HABIT_ID, habit.getId().intValue());
                startActivity(intent);
            }
        });

        registerForContextMenu(habitListView);
    }

    private void showEmptyStateView() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.empty_state, null);

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.empty_state);
        insertPoint.removeAllViews();
        insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));

        TextView header = (TextView) findViewById(R.id.empty_state_header);
        header.setTypeface(font);

        TextView subtext = (TextView) findViewById(R.id.empty_state_subtext);
        subtext.setTypeface(font);
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
        switch (id) {
            case R.id.action_reset:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.reset_all_progress_prompt))
                        .setCancelable(true)
                        .setPositiveButton("Reset all progress", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (Habit habit : habits) {
                                    habit.setCurrentProgress(0);
                                    controller.updateHabit(habit);
                                }

                                String message = "Progress reset for all habits.";
                                Toast.makeText(HabitIndexActivity.this, message, Toast.LENGTH_SHORT).show();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;

//            case R.id.action_settings:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        final Habit habit = habits.get(index);

        String optionTitle = (String) item.getTitle();
        if (optionTitle.equals(getString(R.string.mark_task_done))) {

            Integer newCurrentProgress = habit.getCurrentProgress() + 1;
            habit.setCurrentProgress(newCurrentProgress);
            controller.updateHabit(habit);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if (optionTitle.equals(getString(R.string.edit))) {
            Intent intent = new Intent(this, HabitEditActivity.class);
            intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
            startActivity(intent);
        } else if (optionTitle.equals(getString(R.string.delete))) {
            new AlertDialog.Builder(this)
                    .setMessage("Delete \"" + habit.getName() + "\"?")
                    .setCancelable(true)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            controller.deleteHabit(habit.getId());

                            String message = "\"" + habit.getName() + "\" deleted.";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), HabitIndexActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(HabitIndexActivity.HABIT_ID, habit.getId().intValue());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }

        return true;
    }
}