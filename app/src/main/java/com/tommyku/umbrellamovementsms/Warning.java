package com.tommyku.umbrellamovementsms;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class Warning extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        ((TextView) findViewById(R.id.warning_text)).setMovementMethod(LinkMovementMethod.getInstance());
    }

}
