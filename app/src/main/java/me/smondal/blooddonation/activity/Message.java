package me.smondal.blooddonation.activity;

/**
 * Created by Sudarshan on 10/16/2017.
 */

public class Message {
    private String message;
    private String time;
    private int senderID;
    private int receiverID;
    private int message_type;

    public Message() {
    }

    public Message(String message, int message_type, String time, int senderID, int receiverID) {
        this.message = message;
        this.message_type = message_type;
        this.time = time;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }
}
