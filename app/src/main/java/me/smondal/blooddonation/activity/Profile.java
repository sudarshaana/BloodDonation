package me.smondal.blooddonation.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.smondal.blooddonation.R;
import me.smondal.blooddonation.firebaseTools.SharedPrefManager;
import me.smondal.blooddonation.tools.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static android.R.attr.start;
import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_LOAD_PROFILE;

public class Profile extends AppCompatActivity {

    int[] icon = {R.drawable.ic_name, R.drawable.ic_bloodgroup, R.drawable.ic_email, R.drawable.ic_phone_no, R.drawable.ic_division,
            R.drawable.ic_district, R.drawable.ic_institute, R.drawable.ic_birthday, R.drawable.ic_last_donation, R.drawable.ic_donation_status};
//    String[] item = {"Sudarshan Mondal", "B+", "sudarshaana@gmail.com", "01961954626", "Khulna", "Bagerhat",
//            "North Western University", "10.11.1996", "10.06.2017", "Yes"};

    private String name, bloodGroup, email, phoneNo, division, district, institute, birthday, lastDonation, donationStatus;
    List<String> details = new ArrayList<>();

    private RecyclerView recyclerView;
    private Context context;
    private int userID;
    CollapsingToolbarLayout collapsingToolbar;
    private ProgressDialog progressDialog;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProfile.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        userID = SharedPrefManager.getInstance(context).getUserID();


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProfile);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mLayoutManager);


    }


    private class LoadProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Profile.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
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

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != null) {
                //  Log.d("user", result);

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
                            details.add(name);
                            collapsingToolbar.setTitle(name);
                            // email
                            details.add(jsonObject.getString("email"));

                            // bloodGroup
                            bloodGroup = jsonObject.getString("bloodgroup");
                            if (bloodGroup.equals("")) {
                                details.add("No bloodgroup provided");
                            } else {
                                details.add(bloodGroup);
                            }

                            // phone No
                            phoneNo = jsonObject.getString("phone_no");
                            if (phoneNo.equals("")) {
                                details.add("No phone no. provided");
                            } else {
                                details.add(phoneNo);
                            }

                            // division
                            division = jsonObject.getString("division");
                            if (division.equals("")) {
                                details.add("No division provided");
                            } else {
                                details.add(division);
                            }

                            // division
                            district = jsonObject.getString("district");
                            if (district.equals("")) {
                                details.add("No district provided");
                            } else {
                                details.add(district);
                            }

                            // institute
                            institute = jsonObject.getString("institute");
                            if (institute.equals("")) {
                                details.add("No institute provided");
                            } else {
                                details.add(institute);
                            }

                            //birth_year
                            birthday = jsonObject.getString("birth_year");
                            if (birthday.equals("0")) {
                                details.add("Not Provided");
                            } else {
                                details.add(birthday);
                            }
                            // lastDonation
                            lastDonation = jsonObject.getString("last_donation");
                            if (birthday.equals("0")) {
                                details.add("Not Provided");
                            } else {
                                details.add(lastDonation);
                            }


                            // donation status
                            donationStatus = jsonObject.getString("status");
                            if (birthday.equals("0")) {
                                details.add("No");
                            } else {
                                details.add("Yes");
                            }


                        }


                        setDataToView();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void setDataToView() {

        ProfileAdaptor profileAdaptor = new ProfileAdaptor(getApplicationContext(), icon, details);
        recyclerView.setAdapter(profileAdaptor);

    }


    private class GetDataFromUrl {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {


            RequestBody formBody = new FormBody.Builder()
                    .add("id", userID + "")
                    .build();

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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(context, "On Pause", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (details != null) {
            details.clear();
        }

        //
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = getPrefs.getBoolean("profile", true);

        if (isFirstStart) {

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    // .setView(v)
                    .setTitle("Please update your profile")
                    .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(final DialogInterface dialog) {

                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            Intent intent = new Intent(context, UpdateProfile.class);
                            startActivity(intent);
                        }
                    });
                    Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // TODO Do something

                            //Dismiss once everything is OK.
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            });


            //  Make a new preferences editor
            SharedPreferences.Editor e = getPrefs.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("profile", false);

            //  Apply changes
            e.apply();

        } else {

            if (Utils.isNetworkAvailable(context)) {
                new LoadProfile().execute();

            } else {
                Toast.makeText(context, "Failed to load data.\nCheck internet connection.", Toast.LENGTH_SHORT).show();
            }


        }


    }
}
