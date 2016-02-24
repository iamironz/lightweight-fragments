package com.implimentz.fragments;

import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FragmentMeta(analytic = R.string.some_analytic_title,  //for getting analytic title
        name = R.string.some_fragment_name, //for setting title into toolbar
        isRoot = false, //for screen layer indication (material sandwich/arrow icon)
        analyticHit = true, //allow hit this fragment in analytic
        id = R.integer.some_fragment_id,  //some unique fragment id
        toolbarShadow = false //control toolbar shadow (e.g tabs, inline view elements)
)
@FragmentLayout(R.layout.fragment_search_main_page) // for root view inflating
@FragmentMenu(R.menu.search_menu) //for menu inflating

//Parametrized types for argument/owner determination
public class SomeFragment extends Fragment<Arguments, MainActivity> {

    public SomeFragment() {
        super();
    }

    //passing for getArgs() return parametrized argument
    public SomeFragment(@Nullable Arguments args) {
        super(args);
    }

    //calls while screen orientation is changed
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //calls after @FragmentLayout layout created and added in container
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Arguments arguments) {
        super.onViewCreated(view, arguments);
    }

    //calls after activity created/restored
    @Override
    public void onResume() {
        super.onResume();
    }

    //calls after activity is minimized/overlapped
    @Override
    public void onPause() {
        super.onPause();
    }

    //calls after activity configuration changed/destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //calls after FragmentManager onBackPressed() method called
    // onBackPressed() has no fragment closing expected effect while return 'true'
    @Override
    public boolean hasNotEndedAction() {
        return true;
    }

    //calls after hasNotEndedAction() has return 'true'
    @Override
    public void onActionEndRequired() {
        super.onActionEndRequired();
    }

    //FragmentManager helper for getting current fragment behavior
    @Override
    public boolean isActionModeEnabled() {
        return false;
    }

    //calls after @FragmentMenu created and displayed
    @Override
    public void onMenuCreated(@NotNull Menu menu) {
        super.onMenuCreated(menu);
    }

    //calls after some menu item was clicked
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //calls after back button was perssed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

