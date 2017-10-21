package me.smondal.blooddonation.activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.smondal.blooddonation.R;
import me.smondal.blooddonation.firebaseTools.SharedPrefManager;
import me.smondal.blooddonation.tools.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_MESSAGE_LIST;
import static me.smondal.blooddonation.firebaseTools.EndPoints.URL_MESSAGE_SENT;

public class Messages extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<Message> messageList = new ArrayList<>();
    MessageAdaptor messageAdaptor;

    Button iCanDonateBlood, iCantDonateBlood;

    int userID;
    int isAgreeToDonateBlood = 0;
    String msg;
    String formattedDate;
    int message_type = 0;

    private LinearLayout actionLinearLayout;

    private FloatingActionButton sendFab;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = SharedPrefManager.getInstance(getApplicationContext()).getUserID();

        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setStackFromEnd(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerMessage);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mLayoutManager);


        iCanDonateBlood = (Button) findViewById(R.id.iCanDonateBlood);
        iCantDonateBlood = (Button) findViewById(R.id.iCantDonate);


        iCanDonateBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msg = "I can donate blood";
                Message message = new Message(msg, 1, formattedDate, userID, 0);
                isAgreeToDonateBlood = 1;

                messageList.add(message);
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    new sendMessageToServer().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
                }

                messageAdaptor.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageList.size() - 1);

            }
        });

        iCantDonateBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isNetworkAvailable(getApplicationContext())) {

                    new sendMessageToServer().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
                }

                msg = "Sorry. I am not able to donate blood right now.";
                Message message = new Message(msg, 1, formattedDate, userID, 0);
                isAgreeToDonateBlood = 1;
                messageList.add(message);
                messageAdaptor.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        });

        messageText = (EditText) findViewById(R.id.messageEditText);
        sendFab = (FloatingActionButton) findViewById(R.id.fab);
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msg = messageText.getText().toString();

                if (msg.length() > 0) {
                    Message message = new Message(msg, 0, formattedDate, userID, 0);

                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        isAgreeToDonateBlood = 0;
                        messageList.add(message);
                        messageAdaptor.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(messageList.size() - 1);
                        messageText.setText("");

                        new sendMessageToServer().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        if (Utils.isNetworkAvailable(getApplicationContext())) {
            // load data From Server
            new LoadDataFromServer().execute();
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    private class sendMessageToServer extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            SendDataToUrl getData = new SendDataToUrl();
            String response = null;
            try {
                response = getData.run(URL_MESSAGE_SENT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                //Log.d("Response", result);

                JSONObject mainJson = new JSONObject(result);
                String error = mainJson.getString("error");
                String message = mainJson.getString("message");
                Toast.makeText(Messages.this, message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
                response = getData.run(URL_MESSAGE_LIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                //  Log.d("message", result);
                try {

                    {
                        JSONObject mainJson = new JSONObject(result);
                        JSONArray messageJSON = mainJson.getJSONArray("message");
                        for (int i = 0; i < messageJSON.length(); i++) {

                            JSONObject jsonObject = messageJSON.getJSONObject(i);
                            Message message = new Message();
                            message.setSenderID(Integer.parseInt(jsonObject.getString("sender")));
                            message.setReceiverID(Integer.parseInt(jsonObject.getString("receiver")));
                            message.setTime(jsonObject.getString("time"));
                            message.setMessage(jsonObject.getString("message"));
                            message_type = Integer.parseInt(jsonObject.getString("message_type"));
                            messageList.add(message);

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

        actionLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (message_type == 0) {
            actionLinearLayout.setVisibility(View.GONE);
        } else {
            actionLinearLayout.setVisibility(View.VISIBLE);
        }

        messageAdaptor = new MessageAdaptor(getApplicationContext(), messageList);
        recyclerView.setAdapter(messageAdaptor);
        recyclerView.smoothScrollToPosition(messageList.size());

    }


    private class GetDataFromUrl {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDate = df.format(c.getTime());

            RequestBody formBody = new FormBody.Builder()
                    .add("user_id", userID + "")
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

    private class SendDataToUrl {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDate = df.format(c.getTime());

            RequestBody formBody = new FormBody.Builder()
                    .add("user_id", userID + "")
                    .add("message", msg)
                    .add("message_type", message_type + "")
                    .add("agree", String.valueOf(isAgreeToDonateBlood))
                    .add("time", formattedDate)
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


}
