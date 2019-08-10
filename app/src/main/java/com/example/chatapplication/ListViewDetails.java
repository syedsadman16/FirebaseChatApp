
package com.example.chatapplication;

public class ListViewDetails {

    String contactUsername, contactPicture, contactName, lastMessage;

    public ListViewDetails(String contactUsername, String contactName, String lastMessage) {
        this.contactName = contactName;
        this.contactUsername = contactUsername;
        this.lastMessage = lastMessage;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactUsername(){ return contactUsername; }

    public String getContactPicture() {
        return contactPicture;
    }

    public String getLastMessage() {
        return lastMessage;
    }

}
