package com.husky.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * @author husky
 */
public class ActivityTest extends ActivityUnitTestCase<MainActivity> {

    private Intent mIntent;

    public ActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mIntent = new Intent(getInstrumentation().getTargetContext(),MainActivity.class);
    }

    


}
