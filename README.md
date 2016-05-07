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

    manager.closeFragment(MainFragment.class);    


**To close by instance:**

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
    
    
   
Fragment implementation:
--------

[Fragment implementation example]
(https://github.com/Iamironz/lightweight-fragments/blob/master/src/test/java/com/implimentz/fragments/SomeFragment.java)

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
