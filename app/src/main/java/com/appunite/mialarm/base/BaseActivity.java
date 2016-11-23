package com.appunite.mialarm.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Resources getResources() {
        if (getApplication() != null) {
            return getApplication().getResources();
        } else {
            return super.getResources();
        }
    }

    protected abstract void initDagger();

}
