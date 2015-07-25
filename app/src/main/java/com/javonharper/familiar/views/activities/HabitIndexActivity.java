package com.javonharper.familiar.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.views.widgets.FloatingActionButton;
import com.javonharper.familiar.models.Habit;
import com.javonharper.familiar.models.HabitComparator;
import com.javonharper.familiar.daos.HabitController;
import com.javonharper.familiar.views.adapters.HabitsAdapter;
import com.javonharper.familiar.R;

import java.util.Collections;
import java.util.List;


public class HabitIndexActivity extends Activity {

    public static String HABIT_ID = "habitId";
    int prevVisibleItem;
    FloatingActionButton fabButton;
    private List<Habit> habits;
    private HabitController controller;
    private ListView habitListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_index);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        controller = new HabitController(this);
        initializeFABButton();
        habitListView = (ListView) findViewById(R.id.habit_list);

    }

    private void initializeFABButton() {
        fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.new_icon))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitIndexActivity.this, HabitNewActivity.class);
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
        } else {
            addFooterView();
        }

        Collections.sort(habits, new HabitComparator());
        HabitsAdapter adapter = new HabitsAdapter(this, habits);

        habitListView.setAdapter(adapter);


        habitListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (prevVisibleItem != firstVisibleItem) {
                    if (prevVisibleItem < firstVisibleItem) {
                        fabButton.hideFloatingActionButton();
                    } else {
                        fabButton.showFloatingActionButton();
                    }

                    prevVisibleItem = firstVisibleItem;
                }
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

    private void addFooterView() {

        if (findViewById(R.id.reset_progress) != null) {
            return;
        }

        View footerView = ((LayoutInflater) HabitIndexActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reset_progress, null, false);
        habitListView.addFooterView(footerView);

        Button resetButton = (Button) findViewById(R.id.reset_progress);
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.body_font));

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HabitIndexActivity.this)
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
            }
        });
        resetButton.setTypeface(font);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(habits.get(info.position).getName());
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
        } else if (optionTitle.equals(getString(R.string.start_timer))) {
            Intent intent = new Intent(this, HabitTimerActivity.class);
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
                            Toast.makeText(HabitIndexActivity.this, message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(HabitIndexActivity.this, HabitIndexActivity.class);
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