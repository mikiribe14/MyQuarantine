package com.example.myquarantine.application.home;

import com.google.android.material.navigation.NavigationView;

public interface OnListItemClickListener extends NavigationView.OnNavigationItemSelectedListener {
    void onListItemClick(int clickedItemIndex);
    void onLongListItemClick(int clickedItemIndex);

}
