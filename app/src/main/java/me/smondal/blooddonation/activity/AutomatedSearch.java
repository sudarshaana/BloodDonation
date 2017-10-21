package me.smondal.blooddonation.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.smondal.blooddonation.R;
import me.smondal.blooddonation.firebaseTools.SharedPrefManager;
import me.smondal.blooddonation.tools.Utils;

public class AutomatedSearch extends AppCompatActivity {

    private Spinner bloodSpinner, districtSpinner, divisionSpinner;
    Button datePicker, timepicker, submitButton;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // params to send to the server
    String date = "", time = "";
    String userID, bloodGroup, district, division, dateTime, hospital, problem, detailsProblem;

    EditText hospitalEditText, problemEditText, detailsProblemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mated_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AutomatedSearch.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                datePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AutomatedSearch.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timepicker.setText(hourOfDay + ":" + minute);
                                time = hourOfDay + ":" + minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkAvailable(getApplicationContext())) {

                    userID = SharedPrefManager.getInstance(getApplicationContext()).getUserID() + "";

                    //String userID, bloodGroup, district, division, dateTime, hospital, problem, detailsProblem;
                    bloodGroup = String.valueOf(bloodSpinner.getSelectedItem());
                    division = String.valueOf(divisionSpinner.getSelectedItem());
                    district = String.valueOf(districtSpinner.getSelectedItem());

                    hospital = hospitalEditText.getText().toString();
                    problem = problemEditText.getText().toString();
                    detailsProblem = detailsProblemEditText.getText().toString();
                    dateTime = date + " " + time;

                    if (time.length() > 0 && date.length() > 0 && hospital.length() > 0 && problem.length() > 0 && detailsProblem.length() > 0) {
                        // request to the server
                        Log.d("Strings", userID + " " + bloodGroup + " " + division + " " + district + " " + dateTime + " " + hospital + " " + problem + " " + detailsProblem);

                    } else {
                        Toast.makeText(AutomatedSearch.this, "Fill the information correctly", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AutomatedSearch.this, "Network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {


        // spinners
        bloodSpinner = (Spinner) findViewById(R.id.bloodgrounSelection);
        districtSpinner = (Spinner) findViewById(R.id.district);
        divisionSpinner = (Spinner) findViewById(R.id.division);
        locationSelectionSpinner();

        // edit texts
        problemEditText = (EditText) findViewById(R.id.patientsCondition);
        hospitalEditText = (EditText) findViewById(R.id.patientsLocation);
        detailsProblemEditText = (EditText) findViewById(R.id.problemDetails);


        // dates
        datePicker = (Button) findViewById(R.id.datePicker);
        timepicker = (Button) findViewById(R.id.timePicker);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    private void locationSelectionSpinner() {

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // getting selected item string
                String selectedDivision = String.valueOf(divisionSpinner.getSelectedItem());

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, DistrictSelector.getDistrict(selectedDivision));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                districtSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
