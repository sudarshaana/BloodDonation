package me.smondal.blooddonation.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.smondal.blooddonation.R;

public class CallDialogue extends AppCompatActivity {

    private Button yeaButton, noButton;
    private TextView message;
    private String phoneNo, nameOfDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_dialogue);

        yeaButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);
        message = (TextView) findViewById(R.id.titleDialog);

        phoneNo = getIntent().getStringExtra("PhoneNo");
        nameOfDonor = getIntent().getStringExtra("name");
        message.setText("Make a call to " + nameOfDonor + "?");

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        yeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                startActivity(intent);
                finish();
            }
        });
    }
}
