package me.smondal.blooddonation.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.smondal.blooddonation.R;
import me.smondal.blooddonation.tools.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static me.smondal.blooddonation.activity.VARS.BLOODGROUP;
import static me.smondal.blooddonation.activity.VARS.DISTRICT;
import static me.smondal.blooddonation.activity.VARS.DIVIDION;
import static me.smondal.blooddonation.activity.VARS.INSTITUTE;
import static me.smondal.blooddonation.activity.VARS.SEARCH_TYPE;
import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_DONOR_LIST;
import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_MESSAGE_LIST;

public class SearchResults extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private List<Donor> donorList = new ArrayList<>();

    // type 0 means search by area, 1 means search by institute
    private int searchType = 0;
    private String bloodGroup, institute, division, district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();

        getDataFromIntent();

        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.searchResult);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mLayoutManager);


        if (Utils.isNetworkAvailable(context)) {
            new SearchBlood().execute();

        } else {
            Toast.makeText(context, "No network available", Toast.LENGTH_SHORT).show();
        }


    }

    private void getDataFromIntent() {

        searchType = getIntent().getIntExtra(SEARCH_TYPE, 0);
        bloodGroup = getIntent().getStringExtra(BLOODGROUP);
        institute = getIntent().getStringExtra(INSTITUTE);
        district = getIntent().getStringExtra(DISTRICT);
        division = getIntent().getStringExtra(DIVIDION);
    }

    private class SearchBlood extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            GetDataFromUrl getData = new GetDataFromUrl();
            String response = null;
            try {
                response = getData.run(URL_DONOR_LIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d("message", result);
                if (result.contains("\"donors\": []")) {
                    Toast.makeText(context, "No donor found", Toast.LENGTH_SHORT).show();
                }
                try {

                    {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray messageJSON = mainJson.getJSONArray("donors");
                        for (int i = 0; i < messageJSON.length(); i++) {

                            JSONObject jsonObject = messageJSON.getJSONObject(i);
                            Donor donor = new Donor();
                            donor.setId(Integer.parseInt(jsonObject.getString("id")));
                            donor.setName(jsonObject.getString("name"));
                            donor.setBloodGroup(jsonObject.getString("bloodgroup"));
                            donor.setPhoneNo(jsonObject.getString("phone_no"));
                            donor.setInstitute(jsonObject.getString("institute"));
                            donorList.add(donor);

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

        SearchBloodAdaptor adaptor = new SearchBloodAdaptor(context, donorList);
        recyclerView.setAdapter(adaptor);

    }


    private class GetDataFromUrl {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {


            RequestBody formBody = new FormBody.Builder()
                    .add("type", searchType + "")
                    .add("institute", institute)
                    .add("bloodgroup", bloodGroup)
                    .add("division", division)
                    .add("district", district)
                    .build();
            Log.d("Params", searchType + " " + institute + " " + bloodGroup + " " + division + " " + district);

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

}
