package com.javonharper.familiar.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    public static String HABIT_ID = "habitId";

    @Bind(R.id.habit_list) ListView habitListView;
    @Bind(R.id.empty_state) RelativeLayout emptyState;

    private HabitController mController;
    private List<Habit> mHabits;


    int prevVisibleItem;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_index);

        ButterKnife.bind(this);

        hideActionBarIcon();

        floatingActionButton = createFloatingActionButton();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitIndexActivity.this, HabitNewActivity.class);
                startActivity(intent);
            }
        });

        mController = new HabitController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHabits = mController.getAllHabits();
        initializeEmptyState();

        if (mHabits.size() != 0) {
            addFooterView();
        }

        Collections.sort(mHabits, new HabitComparator());
        HabitsAdapter adapter = new HabitsAdapter(this, mHabits);

        habitListView.setAdapter(adapter);

        habitListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (prevVisibleItem != firstVisibleItem) {
                    if (prevVisibleItem < firstVisibleItem) {
                        floatingActionButton.hideFloatingActionButton();
                    } else {
                        floatingActionButton.showFloatingActionButton();
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

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HabitIndexActivity.this)
                        .setMessage(getString(R.string.alert_message_reset_progress))
                        .setCancelable(true)
                        .setPositiveButton("Reset all progress", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (Habit habit : mHabits) {
                                    habit.setCurrentProgress(0);
                                    mController.updateHabit(habit);
                                }

                                String message = getString(R.string.progress_reset_confirm);
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
    }

    private void initializeEmptyState() {
        if (mHabits.size() == 0) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }
}