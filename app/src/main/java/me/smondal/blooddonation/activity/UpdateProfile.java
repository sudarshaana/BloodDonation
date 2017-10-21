package me.smondal.blooddonation.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import me.smondal.blooddonation.R;
import me.smondal.blooddonation.firebaseTools.SharedPrefManager;
import me.smondal.blooddonation.tools.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_LOAD_PROFILE;
import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_UPDATE_PROFILE;

public class UpdateProfile extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneNoEditText, birthyearEditText, instituteEditText;
    private Spinner divisionSpinner, districtSpinner, bloodGroupSpinner, donatingStatusSpinner;
    private Button lastDonationDateButton, updateButton;

    private String name, email, phoneNo, birthYear, division, district, bloodGroup, institute, donationStatus, lastDonationDate;

    private Context context;
    private int userID;
    private int mYear, mMonth, mDay;
    String date = "";
    private int request_type; // which type of request; load profile or update profile; 1 for update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        userID = SharedPrefManager.getInstance(context).getUserID();
        request_type = 0;

        initVars();

        if (Utils.isNetworkAvailable(context)) {
            new LoadDataFromServer().execute();

        } else {
            Toast.makeText(context, "Failed to load data.\nCheck internet connection.", Toast.LENGTH_SHORT).show();
        }
        spinnerItemSelection();


        lastDonationDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                lastDonationDateButton.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_type = 1;

                //getting all update values
                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                bloodGroup = String.valueOf(bloodGroupSpinner.getSelectedItem());
                phoneNo = phoneNoEditText.getText().toString();
                district = String.valueOf(districtSpinner.getSelectedItem());
                division = String.valueOf(divisionSpinner.getSelectedItem());
                birthYear = birthyearEditText.getText().toString();
                donationStatus = String.valueOf(donatingStatusSpinner.getSelectedItem());

                if (name.length() > 0 && email.length() > 0 && phoneNo.length() > 0 && birthYear.length() > 0 && date.length() > 0) {

                    if (Utils.isNetworkAvailable(context)) {
                        new UpdateProfileRequest().execute();
                    } else {
                        Toast.makeText(context, "Failed to load data.\nCheck internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void spinnerItemSelection() {

        bloodGroup = String.valueOf(bloodGroupSpinner.getSelectedItem());
        donationStatus = String.valueOf(donatingStatusSpinner.getSelectedItem());

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

    private class LoadDataFromServer extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            GetDataFromUrl getData = new GetDataFromUrl();
            String response = null;
            try {
                response = getData.run(URL_LOAD_PROFILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                //Log.d("user", result);

                if (result.contains("\"donors\": []")) {
                    Toast.makeText(context, "No donor found", Toast.LENGTH_SHORT).show();
                }
                try {

                    {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray messageJSON = mainJson.getJSONArray("user");
                        for (int i = 0; i < messageJSON.length(); i++) {

                            JSONObject jsonObject = messageJSON.getJSONObject(i);

                            // name
                            name = jsonObject.getString("name");
                            nameEditText.setText(name);

                            // email
                            email = jsonObject.getString("email");
                            emailEditText.setText(email);

                            // bloodGroup
                            bloodGroup = jsonObject.getString("bloodgroup");


                            // phone No
                            phoneNo = jsonObject.getString("phone_no");
                            if (phoneNo.length() > 0) {
                                phoneNoEditText.setText(phoneNo);
                            }

                            // division
                            division = jsonObject.getString("division");


                            // division
                            district = jsonObject.getString("district");


                            // institute
                            institute = jsonObject.getString("institute");
                            if (institute.length() > 0) {
                                instituteEditText.setText(institute);
                            }

                            //birth_year
                            birthYear = jsonObject.getString("birth_year");
                            if (birthYear.length() > 0) {
                                birthyearEditText.setText(birthYear);
                            }

                            // lastDonation
                            lastDonationDate = jsonObject.getString("last_donation");
                            if (lastDonationDate.length() > 0) {
                                lastDonationDateButton.setText(lastDonationDate);
                            }


                            // donation status
                            donationStatus = jsonObject.getString("status");


                        }


                        // setDataToView();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class UpdateProfileRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            GetDataFromUrl getData = new GetDataFromUrl();
            String response = null;
            try {
                response = getData.run(URL_UPDATE_PROFILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d("user", result);

//                if (result.contains("\"donors\": []")) {
//                    Toast.makeText(context, "No donor found", Toast.LENGTH_SHORT).show();
//                }
                try {

                    {
                        JSONObject mainJson = new JSONObject(result);
                        //mainJson.getString("message");
                        boolean status = mainJson.getBoolean("error");
                        String message = mainJson.getString("msg");


                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(context, Profile.class);
//                            startActivity(intent);
                            finish();
                        }


                        // setDataToView();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class GetDataFromUrl {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {

            RequestBody formBody = null;
            if (request_type == 1) {
                formBody = new FormBody.Builder()
                        .add("id", userID + "")
                        .add("name", name)
                        .add("bloodgroup", bloodGroup)
                        .add("email", email)
                        .add("phone_no", phoneNo)
                        .add("division", division)
                        .add("district", district)
                        .add("institute", institute)
                        .add("birth_year", birthYear)
                        .add("last_donation", date)
                        .add("status", donationStatus)
                        .build();
            } else {
                formBody = new FormBody.Builder()
                        .add("id", userID + "")
                        .build();
            }


            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }

    }


    private void initVars() {
        nameEditText = (EditText) findViewById(R.id.name);
        emailEditText = (EditText) findViewById(R.id.email);
        phoneNoEditText = (EditText) findViewById(R.id.phoneNo);
        birthyearEditText = (EditText) findViewById(R.id.birthyear);
        instituteEditText = (EditText) findViewById(R.id.institute);

        divisionSpinner = (Spinner) findViewById(R.id.division);
        districtSpinner = (Spinner) findViewById(R.id.district);
        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSelection);
        donatingStatusSpinner = (Spinner) findViewById(R.id.donatingStatus);
        lastDonationDateButton = (Button) findViewById(R.id.lastDonationDate);

        updateButton = (Button) findViewById(R.id.updateButton);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

}
