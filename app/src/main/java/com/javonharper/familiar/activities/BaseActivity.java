package com.javonharper.familiar.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;

public abstract class BaseActivity extends Activity {
    public void hideActionBarIcon() {
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }
}
