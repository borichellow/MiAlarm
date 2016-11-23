package com.appunite.mialarm.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
    }

//    public boolean isNetworkAvailable() {
//        if (ConnectivityHelper.isNetworkAvailable(this)) {
//            return true;
//        } else {
//            SnackbarHelper.showErrorSnackBar(findViewById(android.R.id.content),
//                    getString(R.string.error_network_issue));
//            return false;
//        }
//    }

//    protected void setBottomNavigator() {
//        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
//        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabSelectedListener() {
//            @Override
//            public void onMenuItemSelected(int itemId) {
//                if (itemId == R.id.search_item) {
//
//                } else if (itemId == R.id.tickets_item) {
//
//                } else if (itemId == R.id.login_item) {
//
//                } else if (itemId == R.id.profile_item) {
//
//                }
//            }
//        });
//    }

    protected abstract void initDagger();

}
