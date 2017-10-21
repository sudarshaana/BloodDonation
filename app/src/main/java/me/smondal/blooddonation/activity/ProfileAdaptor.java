package me.smondal.blooddonation.activity;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.smondal.blooddonation.R;

/**
 * Created by Sudarshan on 10/16/2017.
 */

class ProfileAdaptor extends RecyclerView.Adapter<ProfileAdaptor.ViewHolder> {

    private Context context;
    int[] icon;
    List<String> text;

    public ProfileAdaptor(Context context, int[] icon, List<String> text) {
        this.context = context;
        this.icon = icon;
        this.text = text;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item_card, parent, false);
        return new ViewHolder(v);

    }

//    @Override
//    public int getItemViewType(int position) {
//        Message message = messageList.get(position);
//        if (message.getSenderID() == 0) {
//            return BOT;
//        } else {
//            return USER;
//        }
//    }

    @Override
    public void onBindViewHolder(ProfileAdaptor.ViewHolder holder, int position) {


        holder.name.setText(text.get(position));
        Picasso.with(context).load(icon[position]).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            icon = (ImageView) itemView.findViewById(R.id.icon);

        }
    }
}


