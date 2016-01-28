# lightweight-fragments
Lightweight android fragment implementation. Uses base activity stack lifecycle

Using:
--------

**Create fragment manager instance:**

    manager = new FragmentManager(this, R.id.container, toolbar);
    manager.setStackChangeListener(new StackChangeListener() {
        @Override
        public void onStackChanged(Fragment fragment, FragmentMeta meta) {
            //do something with stack changes
        }
    });
    
        
**To opening fragment by instance:**

    manager.openFragment(new MainFragment());

    
**To close fragment:**

    final Fragment fragment = new MainFragment();
    manager.closeFragment(fragment);


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
        manager.destroyStack(); //do not use into onDestroy invocation for two-way orientation!!!
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
    
**Menu creating and menu item click handle:**

    @Override
    public int onCreateOptionMenu(@NonNull Menu menu) {
        return R.menu.main;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle with some actions
        return super.onOptionsItemSelected(item);
    }
    
**Handle not end fragment actions:**

    b = true;

    @Override
    public boolean hasNotEndedAction() {
        return b;
    }

    @Override
    public void onActionEndRequired() {
        //show exit confirmation dialog (for example)
        b = false;
    }