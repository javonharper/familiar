package com.javonharper.familiar.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.javonharper.familiar.FloatingActionButton;
import com.javonharper.familiar.Habit;
import com.javonharper.familiar.HabitComparator;
import com.javonharper.familiar.HabitController;
import com.javonharper.familiar.HabitsAdapter;
import com.javonharper.familiar.R;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HabitIndexActivity extends BaseActivity {
    @Bind(R.id.habit_list) ListView habitListView;

    private HabitController controller;

    public static String HABIT_ID = "habitId";
    int prevVisibleItem;
    FloatingActionButton fabButton;
    private List<Habit> habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_index);
        ButterKnife.bind(this);

        hideActionBarIcon();

        createFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitIndexActivity.this, HabitNewActivity.class);
                startActivity(intent);
            }
        });

        controller = new HabitController(this);
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
                        .setMessage(getString(R.string.alert_message_reset_progress))
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
}