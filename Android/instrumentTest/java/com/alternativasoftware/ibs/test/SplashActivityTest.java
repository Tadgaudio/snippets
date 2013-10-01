package com.alternativasoftware.ibs.test;

import android.R;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.alternativasoftware.ibs.view.SplashActivity;
/**
 * Created by tadeu.gaudio on 14/08/13.
 */
public class SplashActivityTest extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Activity mActivity;
    private Instrumentation mInstrumentation;
    public SplashActivityTest() {
        super(SplashActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    @SmallTest
    public void testLayout() {

//        buttonId = com.vogella.android.test.simpleactivity.R.id.button1;
//        assertNotNull(activity.findViewById(buttonId));
//        Button view = (Button) activity.findViewById(buttonId);
//        assertEquals("Incorrect label of the button", "Start", view.getText());
    }

    @SmallTest
    public void testIntentTriggerViaOnClick() {
       // buttonId = com.vogella.android.test.simpleactivity.R.id.button1;
      //  Button view = (Button) activity.findViewById(buttonId);
      //  assertNotNull("Button not allowed to be null", view);

//        // You would call the method directly via
//        getActivity().onClick(view);
//
//        // TouchUtils cannot be used, only allowed in
//        // InstrumentationTestCase or ActivityInstrumentationTestCase2
//
//        // Check the intent which was started
//        Intent triggeredIntent = getStartedActivityIntent();
//        assertNotNull("Intent was null", triggeredIntent);
//        String data = triggeredIntent.getExtras().getString("URL");

        assertEquals("Incorrect data passed via the intent",
                "http://www.vogella.com", "tadeu");
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    public void testCarregarDados() throws Exception {

    }

}
