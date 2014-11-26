package com.tommyku.umbrellamovementsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private SharedPreferences settings;

    private final String SETTING = "umbrellaMovementSMS_Setting";
    private final String SETTING_BEFORE = "setting_before";
    private final String SETTING_CHIN_NAME = "setting_chin_name";
    private final String SETTING_ENG_NAME = "setting_eng_name";
    private final String SETTING_LOCATION = "setting_location";
    private final String SETTING_GENDER = "setting_location";
    private final String SETTING_AGE = "setting_age";
    private final String SETTING_TEL = "setting_tel";
    private final String SETTING_HKID = "setting_hkid";

    private SmsManager smsManager;

    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(SETTING, 0);

        // check if someone has set it, go to that activity if not set yet
        if (!hasSettingBefore()) {
            Intent intent = new Intent(this, Settings.class);
            // no need to put extra
            startActivity(intent);
        }

        ((Button)findViewById(R.id.send)).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if someone has set it, go to that activity if not set yet
        if (!hasSettingBefore()) {
            // show the text
            ((TextView) findViewById(R.id.textView3)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.sms_sample)).setVisibility(View.GONE);
        } else {
            // hide the hint
            ((TextView) findViewById(R.id.textView3)).setVisibility(View.GONE);

            // fill in the details
            String chin_name, eng_name, tel, hkid, age, gender, location;
            chin_name = settings.getString(SETTING_CHIN_NAME, "empty");
            eng_name = settings.getString(SETTING_ENG_NAME, "empty");
            tel = settings.getString(SETTING_TEL, "empty");
            hkid = settings.getString(SETTING_HKID, "empty");
            age = settings.getString(SETTING_AGE, "empty");
            gender = settings.getString(SETTING_GENDER, "empty");
            location = settings.getString(SETTING_LOCATION, "empty");

            msg = "求助:\n"+
                    getString(R.string.i_have_been_arrested)+"\n"+
                    location+"\n"+
                    chin_name+"\n"+
                    eng_name+"\n"+
                    age+","+gender+"\n"+
                    hkid+"\n"+
                    getString(R.string.tel)+": "+tel;

            ((TextView) findViewById(R.id.sample)).setText(msg);

            // display the preset message
            ((LinearLayout) findViewById(R.id.sms_sample)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(this, Settings.class);
                // no need to put extra
                startActivity(intent);
                break;
            case R.id.shopping_warning:
                intent = new Intent(this, Warning.class);
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasSettingBefore() {
        return settings.getBoolean(SETTING_BEFORE, false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send) {
            doSendSMS();
        }
    }

    private void doSendSMS() {
        if (hasSettingBefore()) {
            if (msg.length() > 139) {
                Toast.makeText(this, "文字太多", Toast.LENGTH_SHORT).show();
                return;
            }

            smsManager = SmsManager.getDefault();
            try {
                // smsManager.sendTextMessage("-", null, msg, null, makeDeliveredIntent());
                Toast.makeText(getApplicationContext(), "送出中!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "傳送失敗！", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private PendingIntent makeDeliveredIntent() {
        Intent intent = new Intent();
        return PendingIntent.getBroadcast(this, RESULT_OK, intent, 0);
    }
}
