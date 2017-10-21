package me.smondal.blooddonation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import me.smondal.blooddonation.R;

import static me.smondal.blooddonation.activity.VARS.BLOODGROUP;
import static me.smondal.blooddonation.activity.VARS.DISTRICT;
import static me.smondal.blooddonation.activity.VARS.DIVIDION;
import static me.smondal.blooddonation.activity.VARS.INSTITUTE;
import static me.smondal.blooddonation.activity.VARS.SEARCH_TYPE;

public class SearchBlood extends AppCompatActivity {

    private Button submitButton;
    private RadioGroup radioGroup;
    private RadioButton areaRadio, instituteLocation;
    private EditText instituteEditText;
    private Spinner bloodGroupSpinner, divisionSpinner, districtSpinner;

    private String divisionName, districtName, instituteName, bloodGroup;
    private CardView areaCard, instituteCard;
    private int search_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        radioGroupEventHandel();
        locationSelectionSpinner();
        submitSearch();

    }

    private void submitSearch() {

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bloodGroup = String.valueOf(bloodGroupSpinner.getSelectedItem());
                divisionName = String.valueOf(divisionSpinner.getSelectedItem());
                districtName = String.valueOf(districtSpinner.getSelectedItem());
                instituteName = instituteEditText.getText().toString();
                if (instituteName.length() == 0) {
                    // putting a default value
                    instituteName = "North Western University";
                }

                Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                intent.putExtra(SEARCH_TYPE, search_type);
                intent.putExtra(BLOODGROUP, bloodGroup);
                intent.putExtra(INSTITUTE, instituteName);
                intent.putExtra(DISTRICT, districtName);
                intent.putExtra(DIVIDION, divisionName);
                startActivity(intent);
            }
        });
    }

    private void locationSelectionSpinner() {

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String divisionSelection = String.valueOf(divisionSpinner.getSelectedItem());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, DistrictSelector.getDistrict(divisionSelection));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                districtSpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void radioGroupEventHandel() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.searchByArea:
                        areaCard.setVisibility(View.VISIBLE);
                        instituteCard.setVisibility(View.GONE);
                        search_type = 0;
                        break;
                    case R.id.searchByInstitute:
                        areaCard.setVisibility(View.GONE);
                        instituteCard.setVisibility(View.VISIBLE);
                        search_type = 1;
                        break;

                }
            }
        });
    }

    private void init() {

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        areaRadio = (RadioButton) findViewById(R.id.searchByArea);
        instituteLocation = (RadioButton) findViewById(R.id.searchByInstitute);

        instituteEditText = (EditText) findViewById(R.id.instituteName);

        bloodGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSelection);
        divisionSpinner = (Spinner) findViewById(R.id.division);
        districtSpinner = (Spinner) findViewById(R.id.district);

        areaCard = (CardView) findViewById(R.id.areaLayout);
        instituteCard = (CardView) findViewById(R.id.instituteLayout);
    }

}
