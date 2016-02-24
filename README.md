# lightweight-fragments
Lightweight android fragment implementation. Uses base activity stack lifecycle

Using fragment manager:
-----------------------

**Create fragment manager instance:**

    manager = new FragmentManager(this, R.id.container, toolbar);
    manager.setStackChangeListener(new StackChangeListener() {
        @Override
        public void onStackChanged(Fragment fragment, FragmentMeta meta) {
            //do something with stack changes
        }
    });
    
        
**To opening fragment:**

    manager.openFragment(new MainFragment());

    
**To close fragment:**

    final Fragment fragment = new MainFragment();
    manager.closeFragment(fragment);


**To close by instance:**

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
    
    
   
Fragment implementation:
--------
```
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

```

# License

    The MIT License (MIT)

    Copyright (c) 2016 Alex Ironz
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
