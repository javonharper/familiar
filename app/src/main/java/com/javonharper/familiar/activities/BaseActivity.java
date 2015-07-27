package com.javonharper.familiar.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;

import com.javonharper.familiar.FloatingActionButton;
import com.javonharper.familiar.R;

public abstract class BaseActivity extends Activity {
    public void hideActionBarIcon() {
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    public FloatingActionButton createFloatingActionButton() {
        FloatingActionButton fab = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.new_icon))
                .withButtonColor(getResources().getColor(R.color.green))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        return fab;
    }
}
