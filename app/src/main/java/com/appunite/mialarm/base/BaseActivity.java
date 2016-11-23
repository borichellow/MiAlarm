package com.appunite.mialarm.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.appunite.mialarm.R;
import com.appunite.mialarm.helpers.SnackbarHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
    }

//    protected void setupToolbar(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("");
//        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
//    }
//
//    protected void setupToolbarWhite(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("");
//        toolbar.setNavigationIcon(R.drawable.ic_menu_back_blue);
//    }
//
//    protected void setupToolbarWithClose(Toolbar toolbar) {
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("");
//        toolbar.setNavigationIcon(R.drawable.ic_close_white);
//    }

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
