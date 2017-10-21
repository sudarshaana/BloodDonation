package me.smondal.blooddonation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.smondal.blooddonation.R;

/**
 * Created by Sudarshan on 10/18/2017.
 */

public class SearchBloodAdaptor extends RecyclerView.Adapter<SearchBloodAdaptor.ViewHolder> {

    private List<Donor> donorList;
    private Context context;

    public SearchBloodAdaptor(Context context, List<Donor> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @Override
    public SearchBloodAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);
        return new SearchBloodAdaptor.ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(SearchBloodAdaptor.ViewHolder holder, final int position) {


        final Donor donor = donorList.get(position);
        holder.name.setText(donor.getName());
        holder.bloodGroup.setText(donor.getBloodGroup());
        holder.institute.setText(donor.getInstitute());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  showCallDialogBox(donor.getPhoneNo());
                Intent intent = new Intent(context, CallDialogue.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", donor.getName());
                intent.putExtra("PhoneNo", donor.getPhoneNo());
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return donorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, institute, bloodGroup;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            institute = (TextView) itemView.findViewById(R.id.instituteName);
            bloodGroup = (TextView) itemView.findViewById(R.id.bloodGoup);
        }
    }
}


