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

import java.util.List;

import me.smondal.blooddonation.R;

/**
 * Created by Sudarshan on 10/16/2017.
 */

class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.ViewHolder> {

    private List<Message> messageList;
    private Context context;
    public static final int BOT = 0;
    public static final int USER = 1;

    public MessageAdaptor(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BOT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_layout, parent, false);
            return new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_message_layout, parent, false);
            return new ViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSenderID() == 0) {
            return BOT;
        } else {
            return USER;
        }
    }

    @Override
    public void onBindViewHolder(MessageAdaptor.ViewHolder holder, int position) {


        final Message message = messageList.get(position);
        holder.message.setText(message.getMessage());
        holder.time.setText(message.getTime());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message, time;
        private FrameLayout frameLayout;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.message_text_view);
            time = (TextView) itemView.findViewById(R.id.timestamp_text_view);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayoutMain);
            cardView = (CardView) itemView.findViewById(R.id.bubble);

        }


    }
}


