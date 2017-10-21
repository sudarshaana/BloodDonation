package me.smondal.blooddonation.activity;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudarshan on 10/17/2017.
 */

public class DistrictSelector {


    public static List<String> getDistrict(String selectedDivision) {

        List<String> list = new ArrayList<>();

        if (selectedDivision.contentEquals("Dhaka")) {


            list.add("Dhaka");
            list.add("Faridpur");
            list.add("Gazipur");
            list.add("Gopalganj");
            list.add("Jamalpur");
            list.add("Kishoreganj");
            list.add("Madaripur");
            list.add("Manikganj");
            list.add("Munshiganj");
            list.add("Mymensingh");
            list.add("Narayanganj");
            list.add("Narsingdi");
            list.add("Netrakona");
            list.add("Rajbari");
            list.add("Shariatpur");
            list.add("Sherpur");
            list.add("Tangail");

        } else if (selectedDivision.contentEquals("Khulna")) {

            list.add("Bagerhat");
            list.add("Chuadanga");
            list.add("Jessore");
            list.add("Jhenaidah");
            list.add("Khulna");
            list.add("Kushtia");
            list.add("Magura");
            list.add("Meherpur");
            list.add("Narail");
            list.add("Satkhira");
        } else if (selectedDivision.contentEquals("Chittagong")) {

            list.add("Bandarban");
            list.add("Brahmanbaria");
            list.add("Chandpur");
            list.add("Chittagong");
            list.add("Comilla");
            list.add("Cox's Bazar");
            list.add("Feni");
            list.add("Khagrachhari");
            list.add("Lakshmipur");
            list.add("Noakhali");
            list.add("Rangamati");
        } else if (selectedDivision.contentEquals("Rajshahi")) {

            list.add("Bogra");
            list.add("Joypurhat");
            list.add("Naogaon");
            list.add("Natore");
            list.add("Chapainawabganj");
            list.add("Pabna");
            list.add("Rajshahi");
        } else if (selectedDivision.contentEquals("Sylhet")) {

            list.add("Habiganj");
            list.add("Moulvibazar");
            list.add("Sunamganj");
            list.add("Sylhet");
        } else if (selectedDivision.contentEquals("Barisal")) {

            list.add("Barguna");
            list.add("Barisal");
            list.add("Bhola");
            list.add("Jhalokati");
            list.add("Patuakhali");
            list.add("Pirojpur");
        } else if (selectedDivision.contentEquals("Rangpur")) {

            list.add("Sirajganj");
            list.add("Dinajpur");
            list.add("Gaibandha");
            list.add("Kurigram");
            list.add("Lalmonirhat");
            list.add("Nilphamari");
            list.add("Panchagarh");
            list.add("Rangpur");
        }

        return list;
    }
}
