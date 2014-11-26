package com.tommyku.umbrellamovementsms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class Settings extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private final String SETTING = "umbrellaMovementSMS_Setting";
    private final String SETTING_BEFORE = "setting_before";
    private final String SETTING_CHIN_NAME = "setting_chin_name";
    private final String SETTING_ENG_NAME = "setting_eng_name";
    private final String SETTING_LOCATION = "setting_location";
    private final String SETTING_GENDER = "setting_location";
    private final String SETTING_AGE = "setting_age";
    private final String SETTING_TEL = "setting_tel";
    private final String SETTING_HKID = "setting_hkid";

    private String location = "";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner spinner = (Spinner) findViewById(R.id.location);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner genderSpinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> GsAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(GsAdapter);

        settings = getSharedPreferences(SETTING, 0);
        editor = settings.edit();

        ((Button)findViewById(R.id.save)).setOnClickListener(this);

        ((Spinner)findViewById(R.id.gender)).setOnItemSelectedListener(this);
        ((Spinner)findViewById(R.id.location)).setOnItemSelectedListener(this);

        if (settings.getBoolean(SETTING_BEFORE, false)) {
            // has settings, read and write them into the UI
            ((EditText) findViewById(R.id.chin_name)).setText(settings.getString(SETTING_CHIN_NAME,""));
            ((EditText) findViewById(R.id.eng_name)).setText(settings.getString(SETTING_ENG_NAME,""));
            ((EditText) findViewById(R.id.tel)).setText(settings.getString(SETTING_TEL,""));
            ((EditText) findViewById(R.id.hkid)).setText(settings.getString(SETTING_HKID,""));
            ((EditText) findViewById(R.id.age)).setText(settings.getString(SETTING_AGE,""));
            // I know this is messy, but dev speed first
            Spinner theGenderSpinner = ((Spinner) findViewById(R.id.gender));
            theGenderSpinner.setSelection(((ArrayAdapter) theGenderSpinner.getAdapter()).getPosition(settings.getString(SETTING_GENDER,"男")));

            Spinner theLocationSpinner = ((Spinner) findViewById(R.id.location));
            theLocationSpinner.setSelection(((ArrayAdapter) theLocationSpinner.getAdapter()).getPosition(settings.getString(SETTING_LOCATION,"金鐘區")));
        }
    }

    @Override
    public void onClick(View view) {
        Log.v("NOTI", "click handler");
        if (view.getId() == R.id.save) {
            savePref();
        }
    }

    private void savePref() {
        String chin_name, eng_name, tel, hkid, age;

        chin_name = ((EditText) findViewById(R.id.chin_name)).getText().toString();
        eng_name = ((EditText) findViewById(R.id.eng_name)).getText().toString();
        tel = ((EditText) findViewById(R.id.tel)).getText().toString();
        hkid = ((EditText) findViewById(R.id.hkid)).getText().toString();
        age = ((EditText) findViewById(R.id.age)).getText().toString();

        editor.putString(SETTING_CHIN_NAME, chin_name);
        editor.putString(SETTING_ENG_NAME, eng_name);
        editor.putString(SETTING_TEL, tel);
        editor.putString(SETTING_HKID, hkid);
        editor.putString(SETTING_AGE, age);
        editor.putString(SETTING_GENDER, gender);
        editor.putString(SETTING_LOCATION, location);
        editor.putBoolean(SETTING_BEFORE, true);
        editor.commit();

        Log.v("NOTI", "saved");

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v("NOTI", "item selected");
        switch (((Spinner) adapterView).getId()) {
            case R.id.gender:
                gender = (String) adapterView.getItemAtPosition(i);
                Log.v("NOTI", "gender ="+gender);
                break;
            case R.id.location:
                location = (String) adapterView.getItemAtPosition(i);
                Log.v("NOTI", "location ="+location);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
