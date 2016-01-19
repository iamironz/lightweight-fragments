# lightweight-fragments
Lightweight android fragment implementation. Uses base activity stack lifecycle

Using:
--------

**Create fragment manager instance:**

    manager = new FragmentManager(this, R.id.container, toolbar);
    manager.setStackChangeListener(new StackChangeListener() {
        @Override
        public void onStackChanged(Fragment fragment, FragmentMeta meta) {
            Log.d("MainActivity", "onStackChanged: " + meta.toString());
        }
    });
    
        
**To opening fragment by instance:**

    manager.openFragment(new MainFragment(1, false, R.color.colorPrimary));

    
**To close fragment:**

    manager.closeFragment(MainFragment.class);

    
**To pop fragment:**

    manager.popFragment(MainFragment.class);


**Back pressing handling:**

    @Override
    public void onBackPressed() {
    
        //some actions

        if (manager.isActionModeEnabled()) {
            manager.disableActionMode();
            return;
        }

        if (manager.hasNotEndedActions()) {
            manager.onActionEndRequired();
            return;
        }

        if(manager.getStackCount() > 1) {
            manager.closeLastFragment();
            return;
        }

        super.onBackPressed();
        manager.destroyStack(); //do not use stack destroying into onDestroy invocation for two way orientation it destroy all stack!!!
    }
    
**Activity lifecycle handling:**

    @Override
    protected void onResume() {
        manager.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.onPause();
        super.onPause();
    }